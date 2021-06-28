package pollingapp.rassellworld.voicerecorderapp.record

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pollingapp.rassellworld.voicerecorderapp.MainActivity
import pollingapp.rassellworld.voicerecorderapp.R
import pollingapp.rassellworld.voicerecorderapp.database.RecordDatabase
import pollingapp.rassellworld.voicerecorderapp.database.RecordDatabaseDao
import pollingapp.rassellworld.voicerecorderapp.databinding.FragmentRecordBinding
import java.io.File


class RecordFragment : Fragment() {
    private lateinit var viewModel: RecordViewModel
    private lateinit var mainActivity: MainActivity
    private var database: RecordDatabaseDao? = null
    private val MY_PERMISSIONS_RECORD_AUDIO = 123
    private lateinit var playFab: FloatingActionButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentRecordBinding>(
            inflater,
            R.layout.fragment_record,
            container,
            false
        )
        database = context?.let { RecordDatabase.getInstance(it).recordDatabaseDao }

        playFab = binding.fabPlay

        mainActivity = activity as MainActivity

        viewModel = ViewModelProvider(this).get(RecordViewModel::class.java)
        binding.recordViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        if (!mainActivity.isServiceRunning()) {
            viewModel.resetTimer()
        } else {
            binding.fabPlay.setImageResource(R.drawable.outline_stop_circle_black_48)
        }

        binding.fabPlay.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(),
                    android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.RECORD_AUDIO), MY_PERMISSIONS_RECORD_AUDIO)
            } else {
                if (mainActivity.isServiceRunning()) {
                    onRecord(false)
                    viewModel.stopTimer()
                } else {
                    onRecord(true)
                    viewModel.startTimer()
                }
            }
        }

        createChannel(
            getString(R.string.notification_channel_id),
            getString(R.string.notification_channel_name)

        )

        return binding.root
    }

    private fun onRecord(start: Boolean) {
        val intent = Intent(activity, RecordViewModel::class.java)


        if (start) {
            playFab.setImageResource(R.drawable.outline_stop_circle_black_48)
            Toast.makeText(activity, R.string.toast_recording_start, Toast.LENGTH_SHORT).show()

            val folder =
                File(activity?.getExternalFilesDir(null)?.absolutePath.toString() + "/VoiceRecorder")
            if (!folder.exists()) {
                folder.mkdir()
            }

            activity?.startService(intent)
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            playFab.setImageResource(R.drawable.outline_mic_black_48)

            activity?.stopService(intent)
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_RECORD_AUDIO -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onRecord(true)
                    viewModel.startTimer()
                } else {
                    Toast.makeText(
                        activity,
                        getString(R.string.toast_recording_permissions),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }
        }
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
                .apply {
                    setShowBadge(false)
                    setSound(null, null)
                }
            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

}