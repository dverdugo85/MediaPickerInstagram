package me.ningsk.alivec.jni;

import android.media.MediaExtractor;
import android.media.MediaFormat;

/**
 * <p>描述：获取音频或视频的信息<p>
 * 作者：ningsk<br>
 * 日期：2018/11/19 10 53<br>
 * 版本：v1.0<br>
 */
public class VideoUitls {

    private VideoUitls() {
    }

    /**
     * 获取视频信息
     *
     * @param url
     * @return	视频时长（单位微秒）
     */
    public static long getDuration(String url) {
        try {
            MediaExtractor mediaExtractor = new MediaExtractor();
            mediaExtractor.setDataSource(url);
            int videoExt = TrackUtils.selectVideoTrack(mediaExtractor);
            if(videoExt == -1){
                videoExt = TrackUtils.selectAudioTrack(mediaExtractor);
                if(videoExt == -1){
                    return 0;
                }
            }
            MediaFormat mediaFormat = mediaExtractor.getTrackFormat(videoExt);
            long res = mediaFormat.containsKey(MediaFormat.KEY_DURATION) ? mediaFormat.getLong(MediaFormat.KEY_DURATION) : 0;//时长
            mediaExtractor.release();
            return res;
        } catch (Exception e) {
            return 0;
        }
    }
}

