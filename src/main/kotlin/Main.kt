import dji.common.mission.waypoint.*
import dji.common.model.LocationCoordinate2D
import dji.sdk.base.BaseComponent
import dji.sdk.base.BaseProduct
import dji.sdk.mission.waypoint.MissionControl
import dji.sdk.mission.waypoint.WaypointMissionOperator
import dji.sdk.products.Aircraft
import dji.sdk.sdkmanager.SDKInitEvent
import dji.sdk.sdkmanager.SDKManager

fun main() {
    SDKManager.getInstance().initialize(object : SDKManager.SDKManagerCallback {
        override fun onProductDisconnect() {
            TODO("Not yet implemented")
        }

        override fun onProductConnect(var1: BaseProduct?) {
            val aircraft = SDKManager.getInstance().product as? Aircraft
            aircraft?.flightController?.setStateCallback { state ->
                val location = state.aircraftLocation?.let {
                    val latitude = it.latitude
                    val longitude = it.longitude
                    val altitude = it.altitude
//                    aircraft.flightController?.setHomeLocation(LocationCoordinate2D(latitude, longitude)) { error ->
//                        if (error == null) {
//                            println("Home Position установлена вручную")
//                        } else {
//                            println("Ошибка: ${error.description}")
//                        }
//                    }
                    println("Latitude: $latitude, Longitude: $longitude, Altitude: $altitude")
//                    aircraft.flightController?.setStateCallback(null)
                }
            }

            val waypointList = listOf(
                Waypoint(-35.363262, 149.1652372, 5f),
                Waypoint(-35.365262, 149.1652372, 7f),
                Waypoint(-35.378562, 149.1702372, 5f)
            )
            val mission = WaypointMission.Builder().apply {
                waypointList(waypointList)
                waypointCount(waypointList.size)
                autoFlightSpeed(15.0f)
                maxFlightSpeed(15.0f)
                headingMode(WaypointMissionHeadingMode.AUTO)
                flightPathMode(WaypointMissionFlightPathMode.NORMAL)
                finishedAction(WaypointMissionFinishedAction.AUTO_LAND)
            }.build()
            val operator = MissionControl.instance.waypointMissionOperator
            val err = operator.loadMission(mission)
            if (err != null) {
                println("$err, ${err.description}")
            }
            operator.uploadMission { error ->
                if (error == null) {
                    println("Mission uploaded")
//                    operator.startMission { startError ->
//                        println("Start result: $startError")
//                    }
                } else {
                    println("Upload error: ${error}")
                }
            }
        }

        override fun onProductChanged(var1: BaseProduct?) {
            TODO("Not yet implemented")
        }

        override fun onComponentChange(var1: BaseProduct.ComponentKey?, var2: BaseComponent?, var3: BaseComponent?) {
            TODO("Not yet implemented")
        }

        override fun onInitProcess(var1: SDKInitEvent?, var2: Int) {
            TODO("Not yet implemented")
        }
    })
    readln()
}