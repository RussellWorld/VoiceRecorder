package pollingapp.rassellworld.voicerecorderapp.record

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaRecorder
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*
import pollingapp.rassellworld.voicerecorderapp.MainActivity
import pollingapp.rassellworld.voicerecorderapp.R
import pollingapp.rassellworld.voicerecorderapp.database.RecordDatabase
import pollingapp.rassellworld.voicerecorderapp.database.RecordDatabaseDao
import pollingapp.rassellworld.voicerecorderapp.database.RecordingItem
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class RecordService : Service() {

    private var mFileName: String? = null
    private var mFilePatch: String? = null

    private var mRecorder: MediaRecorder? = null

    private var mStartingTimesMillis: Long = 0
    private var mElapsedMillis: Long = 0

    private var mDatabase: RecordDatabaseDao? = null

    private val mJob = Job()
    private val mUiScope = CoroutineScope(Dispatchers.Main + mJob)

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mDatabase = RecordDatabase.getInstance(application).recordDatabaseDao

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
       startRecording()
        return START_STICKY
    }

    private fun startRecording() {
        setFileNameAndPath()

        mRecorder = MediaRecorder()
        mRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mRecorder?.setOutputFile(mFilePatch)
        mRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mRecorder?.setAudioChannels(1)
        mRecorder?.setAudioEncodingBitRate(192000)


        try {
            mRecorder?.prepare()
            mRecorder?.start()
            mStartingTimesMillis = System.currentTimeMillis()
            startForeground(1, createNotification())
        } catch (e: IOException) {
            Log.e("RecordService", "prepare failed")
        }
    }

    private fun createNotification(): Notification {
        val mBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(
                applicationContext,
                getString(R.string.notification_channel_id)
            )
                .setSmallIcon(R.drawable.outline_mic_black_48)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_recording))
                .setOngoing(true)
        mBuilder.setContentIntent(
            PendingIntent.getActivities(
                applicationContext, 0, arrayOf(
                    Intent(
                        applicationContext,
                        MainActivity::class.java
                    )
                ), 0
            )
        )
        return mBuilder.build()
    }

    @SuppressLint("SimpleDateFormat")
    private fun setFileNameAndPath() {
        var count = 0
        var f: File
        val dateTime = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(System.currentTimeMillis())

        do {
            mFilePatch = (getString(R.string.default_file_name)
                    + "_" + dateTime + count + ".mp4")
            mFilePatch = application.getExternalFilesDir(null)?.absolutePath
            mFilePatch += "/$mFileName"

            count++

            f = File(mFilePatch)
        } while (f.exists() && !f.isDirectory)
    }

    private fun stopRecording() {
        val recordingItem = RecordingItem()

        mRecorder?.stop()
        mElapsedMillis = System.currentTimeMillis() - mStartingTimesMillis
        mRecorder?.release()
        Toast.makeText(
            this,
            getString(R.string.toast_recording_finish),
            Toast.LENGTH_SHORT
        ).show()

        recordingItem.name = mFileName.toString()
        recordingItem.filePath = mFilePatch.toString()
        recordingItem.length = mElapsedMillis
        recordingItem.time = System.currentTimeMillis()

        mRecorder = null

        try {
            mUiScope.launch {
                withContext(Dispatchers.IO) {
                    mDatabase?.insert(recordingItem)
                }
            }
        } catch (e: Exception) {
            Log.e("RecordService", "exception", e)
        }
    }

    override fun onDestroy() {
        if (mRecorder != null) {
            stopRecording()
        }
        super.onDestroy()
    }
}