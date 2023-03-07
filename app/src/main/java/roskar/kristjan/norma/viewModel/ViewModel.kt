package roskar.kristjan.norma.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import roskar.kristjan.norma.database.AppDatabase
import roskar.kristjan.norma.model.Productivity
import roskar.kristjan.norma.repository.ProductivityRepository

class ViewModel(application: Application): AndroidViewModel(application) {


    private val productivityRepository: ProductivityRepository

    init {

        val productivityDao = AppDatabase.getDatabase(application).productivity()


    }

}