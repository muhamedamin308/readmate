    
 class AppConstants {
   static const String appName = 'your app name';
   
   
   
   static List<LanguageModel> languages = [

       LanguageModel(languageName: 'العربية', countryCode: 'ar_Ar', languageCode: 'ar'),
       LanguageModel(languageName: 'English', countryCode: 'US', languageCode: 'en'),

     ];

 }


// todo move it to base model in the base 
class LanguageModel {
  String? languageName;
  String? languageCode;
  String? countryCode;

  LanguageModel({ this.languageName, this.countryCode, this.languageCode});
}

       