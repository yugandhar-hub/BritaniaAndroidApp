# BritaniaAndroidApp

The project contains navigation with two navigation menu's as follows,

1.Admin Reports
2.User Reports

Admin Reports : When click on this. it takes you to Report Filter

Report Filter Screen contains 2 Dropdowns - Department and ReportName. And 3 Buttons - Add Report, Delete Report, and Filter based on the selection.

Get Reports: This button takes you to new screen which contains webview to display report using Tablue Server.


Add Report : Add report button in filter page will take you to the Add Report Screen. This screen contains Department Dropdown and 2 fields one is Report name and another one is URL. These two fields helps user to add multiple records by clicking on ADD button. User can able to see those multiple records below with horizontal scrolling. once user click on ADD REPORT it makes http request using Volley library then returns response that i am showing in the toast.

Delete Report : Delete report button in the filter page will bring you Popup which contains list of report names. User can able to select multiple report names and he can able to search the reports in the popup. Once after selecting reports then can able to click on the DELETE REPORT Button. this button makes http post request using Volley to delete the selected Reports. Before calling api Prompt will appear to get confirmation from the user.

1.Using Latest Android Studio Version.
2.App Compitable up to Latest Android Version
3.Using Volley Library for network operations
