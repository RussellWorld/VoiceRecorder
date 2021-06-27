package pollingapp.rassellworld.voicerecorderapp

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import pollingapp.rassellworld.voicerecorderapp.database.RecordDatabase
import pollingapp.rassellworld.voicerecorderapp.database.RecordDatabaseDao
import pollingapp.rassellworld.voicerecorderapp.database.RecordingItem
import java.io.IOException
import java.lang.Exception
import kotlin.jvm.Throws


@RunWith(AndroidJUnit4::class)
class RecordDatabaseTest {

    private lateinit var recordDatabaseDao: RecordDatabaseDao
    private lateinit var database: RecordDatabase

    @Before
    fun createDB() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, RecordDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        recordDatabaseDao = database.recordDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDB(){
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun testDatabase(){
        recordDatabaseDao.insert(RecordingItem())
        val getCount = recordDatabaseDao.getCount()
        assertEquals(getCount, 1)
    }
}