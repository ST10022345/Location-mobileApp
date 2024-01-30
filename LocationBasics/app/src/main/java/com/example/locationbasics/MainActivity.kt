package com.example.locationbasics

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import android.Manifest
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.locationbasics.ui.theme.LocationBasicsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel : LocationViewModel = viewModel()
            LocationBasicsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                MyApp(viewModel)
                }
            }
        }
    }
}

@Composable
fun MyApp(viewModel: LocationViewModel){
    val context = LocalContext.current
    val locationUtils = LocationUtils(context)
    locationDisplay(locationUtils = locationUtils,viewModel, context = context )
    
}




@Composable
fun locationDisplay(locationUtils: LocationUtils,viewModel: LocationViewModel, context: Context){
    val location = viewModel.Location.value

    val address = location?.let {
        locationUtils.reverseGeocodeLocation(location)
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions(), onResult = {permissions ->
        if(permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true && permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ){
//i have access to location
locationUtils.requestLocationUpdates(viewModel = viewModel)

        }else{
            //ask for permission
            val rationaleRequired = ActivityCompat.shouldShowRequestPermissionRationale( context as MainActivity, Manifest.permission.ACCESS_FINE_LOCATION)||
                    ActivityCompat.shouldShowRequestPermissionRationale( context, Manifest.permission.ACCESS_COARSE_LOCATION)

            if (rationaleRequired){
                Toast.makeText(context,"Location permission required for this feature", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(context,"Location permission required Please enable it in the android settings ", Toast.LENGTH_LONG).show()
            }
        }
    })

Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
    if (location != null){
Text(text = "Address: ${location.latitude} ${location.longitude} \n $address")
    }else{
        Text(text = "Location not available")
    }

    
    Button(onClick = {
        if(locationUtils.hasLocationPerms(context)){
           //update location
            locationUtils.requestLocationUpdates(viewModel)

        }else{
            //request location permission
            requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION))
        }



    }) {
        Text(text = "Get Location")
    }
}
}



