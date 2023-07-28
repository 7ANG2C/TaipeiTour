package com.fang.taipeitour.ui.screen.attraction.guide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import com.fang.taipeitour.ui.theme.TaipeiTourTheme

interface AttractionListener {
    fun onAttraction(index: Int)
}

/**
 * 景點導覽
 */
class AttractionGuideFragment : Fragment(), AttractionListener {

    companion object {
        fun createIntent(): Fragment {
            return AttractionGuideFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // Dispose of the Composition when the view's LifecycleOwner is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                Greeting("Android!",
                    Modifier
                        .fillMaxSize()
                        .background(Color.Gray)
                        .clickable {

                        })
            }
        }
    }

    @Composable
    private fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    private fun GreetingPreview() {
        TaipeiTourTheme {
            Greeting("Android")
        }
    }

    override fun onAttraction(index: Int) {

    }
}
