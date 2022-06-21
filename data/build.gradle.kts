plugins {
    id ("kotlin")
    id ("com.android.lint")
}
dependencies {

    api(project(Modules.launchConstants))
    api(Room.room_common)

}