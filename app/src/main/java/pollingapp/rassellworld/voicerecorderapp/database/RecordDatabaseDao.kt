package pollingapp.rassellworld.voicerecorderapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RecordDatabaseDao {
    @Insert
    fun insert(record: RecordingItem)

    @Update
    fun update(record: RecordingItem)

    @Query("DELETE from recording_table WHERE id = :key")
    fun removeRecord(key: Long?)

    @Query("DELETE from recording_table")
    fun clearAll()

    @Query("SELECT * from recording_table WHERE id = :key")
    fun getRecord(key: Long?): RecordingItem?

    @Query("SELECT * from recording_table ORDER BY id DESC")
    fun getAllRecords(): LiveData<MutableList<RecordingItem>>

    @Query("SELECT COUNT(*) from recording_table")
    fun getCount(): Int

}