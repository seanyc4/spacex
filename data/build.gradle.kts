plugins {
    id ("kotlin")
    id ("com.android.lint")
}
dependencies {

    api(project(Modules.constants))
    api(Room.room_common)

}