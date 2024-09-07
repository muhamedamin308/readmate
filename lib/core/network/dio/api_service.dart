    
import 'package:dio/dio.dart';
import 'package:retrofit/retrofit.dart';
import 'api_constants.dart';
part 'api_service.g.dart'; // todo add YourModel then run flutter pub run build_runner build

@RestApi(baseUrl: ApiConstants.apiBaseUrl)
abstract class ApiService {
  factory ApiService(Dio dio, {String baseUrl}) = _ApiService;
  //todo to run the code generator, execute the following command:
  //todo dart run build_runner build
  @POST(ApiConstants.login)
  Future<YourModel> login(@Body() YourPrameter loginRequestBody,);  // todo add YourModel then run flutter pub run build_runner build
  
}

