 

import 'package:flutter/material.dart';

ThemeData dark = ThemeData(
  fontFamily: 'Roboto',
  primaryColor: const Color(0xFFFF9A62),
  secondaryHeaderColor: const Color(0xffffffff),
  brightness: Brightness.dark,
  cardColor: const Color(0xFF252525),
  hintColor: const Color(0xFFbebebe),
  primaryColorDark :   Color(0xFFFFFFFF),
  disabledColor: const Color(0x33FF9A62),
  shadowColor: Colors.black.withOpacity(0.4),
  pageTransitionsTheme: const PageTransitionsTheme(builders: {
    TargetPlatform.android: ZoomPageTransitionsBuilder(),
    TargetPlatform.iOS: ZoomPageTransitionsBuilder(),
    TargetPlatform.fuchsia: ZoomPageTransitionsBuilder(),
  }),
  textTheme: const TextTheme(
    labelLarge: TextStyle(color: Color(0xFF252525)),
    displayLarge: TextStyle(fontWeight: FontWeight.w300,  ),
    displayMedium: TextStyle(fontWeight: FontWeight.w400,  ),
    displaySmall: TextStyle(fontWeight: FontWeight.w500,  ),

  ),

);


