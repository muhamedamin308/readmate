     
import 'package:flutter/material.dart';
import 'app_localization.dart';

 String? getTranslated(String? key, BuildContext context) {
 String? text = key;
   try{
  text = AppLocalization.of(context)!.translate(key);
 }catch (mError){
  // todo you have to delete this \ first  
     debugPrint('error --- $\mError');
  }
  return text;
  
 }