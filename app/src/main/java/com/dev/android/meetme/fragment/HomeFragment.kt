package com.dev.android.meetme.fragment

import android.os.Binder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import com.bumptech.glide.Glide.init
import com.dev.android.meetme.R
import com.dev.android.meetme.adapter.DatingAdapter
import com.dev.android.meetme.databinding.ActivityMainBinding
import com.dev.android.meetme.databinding.FragmentHomeBinding
import com.dev.android.meetme.model.UserDataModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction


class HomeFragment : Fragment() {
   private lateinit var binding: FragmentHomeBinding
   private lateinit var manager:CardStackLayoutManager
    private val list=ArrayList<UserDataModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentHomeBinding.inflate(layoutInflater)


        getData()

        return binding.root
    }

    private fun init() {
        manager= CardStackLayoutManager(requireContext(),object :CardStackListener{
            override fun onCardDragging(direction: Direction?, ratio: Float) {

            }

            override fun onCardSwiped(direction: Direction?) {
              if(manager.topPosition==list.size){
                    Toast.makeText(requireContext(),"Last card",Toast.LENGTH_SHORT).show()
              }else{

              }
            }

            override fun onCardRewound() {

            }

            override fun onCardCanceled() {

            }

            override fun onCardAppeared(view: View?, position: Int) {

            }

            override fun onCardDisappeared(view: View?, position: Int) {

            }

        })

        manager.setVisibleCount(3)
        manager.setTranslationInterval(0.6f)
        manager.setScaleInterval(0.8f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
    }


    private fun getData() {
        FirebaseDatabase.getInstance().getReference("Users")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("STACK","onDataChange: ${snapshot.toString()}")

                    if(snapshot.exists()){
                        for(data in snapshot.children){
                            val model=data.getValue(UserDataModel::class.java)
                            list.add(model!!)
                        }
                        list.shuffle()
                        init()

                        binding.cardStackView.layoutManager=manager
                        binding.cardStackView.itemAnimator=DefaultItemAnimator()
                        binding.cardStackView.adapter=DatingAdapter(requireContext(),list)

                    }else{
                        Toast.makeText(requireContext(),"something wrong",Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(),error.message,Toast.LENGTH_SHORT).show()
                }

            })
    }

    companion object {

    }
}