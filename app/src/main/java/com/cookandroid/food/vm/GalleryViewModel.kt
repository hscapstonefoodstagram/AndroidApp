package com.cookandroid.food.vm



import android.content.Context
import android.util.Log
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.cookandroid.food.mediacontentresolver.MediaContentResolver
import com.cookandroid.food.model.searchimageItem


class GalleryViewModel(val mediaContentResolver: MediaContentResolver) : ViewModel() {
    private val _isMultiSelect = MutableLiveData<Boolean>().apply {
        value = false
    }
    val isMultiSelect: LiveData<Boolean> = _isMultiSelect
    val _currentSelectedImage = MutableLiveData<String>()


//    /** 여러 사진을 올릴 때 저장하는 리스트*/
//    val selectedPictures = ArrayList<String>()
//    val currentSelectedImage: LiveData<String> = _currentSelectedImage
      var picturesLiveData = ArrayList<SelectedImage>()

    fun selectImage(path: String) {
        _currentSelectedImage.postValue(path)

//        if (_isMultiSelect.value == false) {
//            if (selectedPictures.isNullOrEmpty()) {
//                selectedPictures.add(0, path)
//            } else {
//                selectedPictures[0] = path
//            }
//        } else if (!selectedPictures.contains(path)) {
//            Log.d("", "add $path")
//            selectedPictures.add(path)
//        } else {
//            Log.d("", "rem $path")
//            selectedPictures.remove(path)
//        }
//        refreshSelectList()
    }


//    private fun refreshSelectList() {
//        for (data in picturesLiveData) {
//            data.index.value = 0
//        }
//
//        for (i in 0 until selectedPictures.size) {
//            val path = selectedPictures[i]
//            for (data in picturesLiveData) {
//                if (data.path.value.equals(path)) {
//                    data.index.postValue(i + 1)
//                    continue
//                }
//            }
//        }
//    }


    fun clickMultiSelect() {
        _isMultiSelect.value = !_isMultiSelect.value!!

//        if (_isMultiSelect.value == false) {
//            selectedPictures.removeAll(selectedPictures)
//            selectedPictures.add(currentSelectedImage.value!!)
//        }
//
//        refreshSelectList()
    }

    fun selectPicture(path: String) {
        //selectedPictures.add(SelectedImage.create(selectedPictures.size, path))
    }

//    fun setPicturepaths(picturePath: java.util.ArrayList<String>) {
//        picturesLiveData = picturePath.toSelectImageList()
//    }

    fun sendImageRequest(context: Context) {

        val queue =  Volley.newRequestQueue(context)

        val url = "http://3.134.92.175/img/$_currentSelectedImage"
        val stringRequest = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->

                for (i in 0 until response.length()) {
                    val arrayItem = response.getJSONObject(i)
                    val tempData= searchimageItem(
                        arrayItem.getInt("id"),
                        arrayItem.getString("img_path")

                    )
                    _currentSelectedImage.value = tempData.toString()



                }



            },
            { error ->
                // Handle error


            })

        queue.add(stringRequest)



    }




}

data class SelectedImage(val index: MutableLiveData<Int>, val path: MutableLiveData<String>) {
    companion object {
        fun create(index: Int, path: String): SelectedImage {
            return SelectedImage(MutableLiveData(index), MutableLiveData(path))
        }
    }
}

class GalleryViewModelFactory(val context: Context) : ViewModelProvider.Factory {
    val mediaContentResolver = MediaContentResolver.newInstance(context)
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GalleryViewModel(mediaContentResolver) as T
    }

}

object DataBindingAdapterUtil {
    @JvmStatic
    @BindingAdapter("app:select")
    fun select(view: View, b: Boolean) {
        view.isSelected = b
    }
}