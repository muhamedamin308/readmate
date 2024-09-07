
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
 
 class Routes {
  static const String splash = "splash"; // add your pages like this 
  static const String login = "login"; // add your pages like this 
  static String currentRoute = splash; // add your current route here

  static Route<dynamic> onGenerateRouted(RouteSettings routeSettings) {
    currentRoute = routeSettings.name ?? "";
    switch (routeSettings.name) {
      case splash:
        {
          return SplashPage.route(routeSettings); // handel your page here
        }
        
        
      default:
        {
          return CupertinoPageRoute(
            builder: (context) => const Scaffold(),
          );
        }
    }
  }
}
