package com.aman.localsearchrecycler

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.aman.localsearchrecycler.adapters.UserAdapter
import com.aman.localsearchrecycler.databinding.ActivityMainBinding
import com.aman.localsearchrecycler.databinding.LayoutAddUpdateUserBinding
import com.aman.localsearchrecycler.models.UserModel
import com.aman.localsearchrecycler.utils.ClickType
import com.aman.localsearchrecycler.utils.UserModelClick

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var showUserModelList = ArrayList<UserModel>()
    private var userModelList = ArrayList<UserModel>()
    lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapter(showUserModelList, object: UserModelClick {
            override fun UserModelClick(position: Int, clickType: ClickType){
                when(clickType){
                    ClickType.EDIT-> {
                        var index = userModelList.indexOfFirst { element-> (element.name?.contains((showUserModelList[position].name?:""), true)== true) && (element.address?.contains((showUserModelList[position].address?:""), true)== true) }
                        showDialogFun(index)
                    }
                    ClickType.DELETE->showDialogDelete(position)
                }
            }
        })
        binding.rvUser.adapter = adapter
        binding.rvUser.layoutManager = LinearLayoutManager(this)

        binding.fab.setOnClickListener { view ->
            showDialogFun()
        }

        binding.etSearch.doOnTextChanged { text, start, before, count ->
            var textLength = text?.length?:0
            if(textLength == 0){
                showUserModelList.clear()
                showUserModelList.addAll(userModelList)
            }else if(textLength>3){
                var list = userModelList.filter { element-> (element.name?.contains(binding.etSearch.text.toString(), true) == true || element.phoneNumber?.contains(binding.etSearch.text.toString(), true) == true) }
                showUserModelList.clear()
                showUserModelList.addAll(list)
            }
            adapter.notifyDataSetChanged()

        }
    }

    fun showDialogDelete(position: Int){
        AlertDialog.Builder(this).apply {
            setTitle(resources.getString(R.string.delete_item))
            setMessage(resources.getString(R.string.delete_item_msg))
            setPositiveButton(resources.getString(R.string.yes)){_,_->
                userModelList.removeAt(position)
                clearAndAddList()
            }
            setNegativeButton(resources.getString(R.string.no)){_,_->
            }
            show()
        }
    }

    private fun clearAndAddList() {
        showUserModelList.clear()
        showUserModelList.addAll(userModelList)
        adapter.notifyDataSetChanged()
        binding.etSearch.text.clear()
    }

    fun showDialogFun(position:Int = -1){
        var dialog = Dialog(this)
        var dialogBinding = LayoutAddUpdateUserBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.show()

        dialogBinding.btnAdd.setOnClickListener{
            if(dialogBinding.etName.text.toString().trim().isNullOrEmpty()){
                dialogBinding.etName.error = resources.getString(R.string.enter_name)
                dialogBinding.etName.requestFocus()
            }else if(dialogBinding.etPhoneNumber.text.toString().trim().isNullOrEmpty()){
                dialogBinding.etPhoneNumber.error = resources.getString(R.string.enter_phone_number)
                dialogBinding.etPhoneNumber.requestFocus()
            }else{
                if(position>-1){
                    userModelList[position] = UserModel(position, dialogBinding.etName.text.toString(), dialogBinding.etPhoneNumber.text.toString())
                }else{
                    userModelList.add(UserModel(userModelList.size, dialogBinding.etName.text.toString(), dialogBinding.etPhoneNumber.text.toString()))
                }
                clearAndAddList()
                dialog.dismiss()
            }
        }
        dialogBinding.btnDelete.setOnClickListener {
            showDialogDelete(position)
            dialog.dismiss()
        }
    }


}