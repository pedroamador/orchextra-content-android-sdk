package gigigo.com.vimeolibs;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.vimeo.networking.Configuration;
import com.vimeo.networking.VimeoClient;
import com.vimeo.networking.model.Video;
import okhttp3.CacheControl;
import retrofit2.Response;

/**
 * Created by nubor on 04/10/2017.
 */

public class VimeoManager {
  private static String mNoket_ssecca = "";
  private static String mClient_ID = "";
  private static String mClient_Secret = "";
  private static String mScope = "";
  private static VimeoClient mApiClient;

  //flow open vimeo video
  //1 get videoid
  //2 instanciate VimeoManeger(accesotken)
  //3 Se llama a getVideoVimeoInfo y en el resultado el callback se recibe el VimeoInfo
  //4 hide loading se llama a VimeoExoPlayerActivity.open(vimeoinfo)
  //flow update accessToken
  //1 get new accesstoken in ocm ocnfig
  //2 decrupt accesstoken use then
  //3 keep encrypt in prferences or wherever

  public VimeoManager(VimeoBuilder builder) {
    Configuration.Builder configBuilder;
    if (builder != null) {
      mNoket_ssecca = builder.getNoket();
      mClient_ID = builder.getClient_id();
      mClient_Secret = builder.getClient_secret();
      mScope = builder.getScope();

      if (mNoket_ssecca != null && !mNoket_ssecca.equals("")) {
        configBuilder = new Configuration.Builder(mNoket_ssecca);
        VimeoClient.initialize(configBuilder.build());
        mApiClient = VimeoClient.getInstance();
      } else {
        if (mClient_ID != null
            && !mClient_ID.equals("")
            && mClient_Secret != null
            && !mClient_Secret.equals("")
            && mScope != null
            && !mScope.equals("")) {

          configBuilder = new Configuration.Builder(mClient_ID, mClient_Secret, mScope);
          VimeoClient.initialize(configBuilder.build());
          mApiClient = VimeoClient.getInstance();
        }
      }
    }
  }

  public void getVideoVimeoInfo(final String videoId, final boolean isMobileConection,
      final boolean isWifiConnection, final boolean isFastConnection,
      final VimeoCallback callback) {
    new Thread(new Runnable() {
      @Override public void run() {
        final Response<Video> videoResponse =
            mApiClient.fetchVideoSync("/videos/" + videoId, CacheControl.FORCE_NETWORK, null);
        //todo ejecutar en otro hilo
        new Handler(Looper.getMainLooper()).post(new Runnable() {
          @Override public void run() {
            if (videoResponse != null && videoResponse.body() != null) {
              VimeoInfo info = new VimeoInfo();
              //region  determine quality from connection
              int thumbIdx = 0;
              int videoIdx = 0;
              if (isFastConnection) {
                if (isWifiConnection) {
                  thumbIdx = VimeoQuality.THUMB_HD.ordinal();
                  videoIdx = VimeoQuality.HDFULL.ordinal();
                } else {
                  thumbIdx = VimeoQuality.THUMB_SD.ordinal();
                  videoIdx = VimeoQuality.HDREADY.ordinal();
                }
              } else {
                if (isWifiConnection) {
                  thumbIdx = VimeoQuality.THUMB_SD.ordinal();
                  videoIdx = VimeoQuality.SD.ordinal();
                } else {
                  thumbIdx = VimeoQuality.THUMB_SD.ordinal();
                  videoIdx = VimeoQuality.SDLOW.ordinal();
                }
              }
              //endregion

              //asv new check INDEXs, because some videos dont have all resolutions
              if (videoResponse.body().getDownload() != null
                  && videoResponse.body().files.size() < videoIdx + 1) {
                videoIdx = 0;
              }
              if (videoResponse.body().pictures != null
                  && videoResponse.body().pictures.sizes.size() < thumbIdx + 1) {
                thumbIdx = 0;
              }

              if (videoResponse.body().getDownload() != null
                  && videoResponse.body().getDownload().size() >= videoIdx + 1
                  && videoResponse.body().pictures != null
                  && videoResponse.body().pictures.sizes.size() >= thumbIdx + 1) {
                info.setVideoPath(videoResponse.body().files.get(videoIdx).getLink());
                Log.e("", info.getVideoPath());
                info.setThumbPath(videoResponse.body().pictures.sizes.get(
                    thumbIdx).link); //todo put the best qualty THUMB_HD, check if always have this quality
                Log.e("", info.getThumbPath());
                if (videoResponse.body().width > videoResponse.body().height) {
                  info.setVertical(false);
                } else {
                  info.setVertical(true);
                }
                callback.onSuccess(info);
              } else {
                callback.onError(new Exception(
                    "No data retrive from Vimeo video, maybe this is not a video from provided accesstoken account"
                        + videoId));
              }
            } else {
              callback.onError(new Exception(
                  "No data retrive from Vimeo video, maybe this is not a video from provided accesstoken account"
                      + videoId));
            }
          }
        });
      }
    }).start();
  }

  public void updateAccessToken(String access_token) {
    //todo maybe in the fiture the accesstoken come from de back, DANGER sniffer, ocm config must be
  }
}
