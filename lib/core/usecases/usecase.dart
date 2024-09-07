   
abstract class UseCase<ApiResult, Params> {
  Future<ApiResult> call(Params params);
}
      