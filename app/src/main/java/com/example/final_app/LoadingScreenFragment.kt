package com.example.final_app

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class LoadingScreenFragment : Fragment(R.layout.fragment_loadingscreen) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val wheel = view.findViewById<ImageView>(R.id.wheelImage)
        val rotation = ObjectAnimator.ofFloat(wheel, "rotation", 0f, 600f)
        rotation.duration = 3400
        rotation.repeatCount = ObjectAnimator.INFINITE
        rotation.start()

        val smallWheel = view.findViewById<ImageView>(R.id.smallWheelImage)
        val smallWheelRotation = ObjectAnimator.ofFloat(smallWheel, "rotation", 0f, -600f)
        smallWheelRotation.duration = 3400
        smallWheelRotation.repeatCount = ObjectAnimator.INFINITE
        smallWheelRotation.start()

        // Use a Handler to navigate after a delay
        Handler(Looper.getMainLooper()).postDelayed({
            // Use the NavController to navigate to the MovieListFragment
            findNavController().navigate(R.id.action_loadingScreenFragment_to_movieListFragment)
        }, 3100)
    }
}
