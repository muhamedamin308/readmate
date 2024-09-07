 import 'dart:math';

class AppUtils {
  // Prevent the utility class from being instantiated.
  AppUtils._();


  // Example utility method 2: Generating a random number between min and max.
  static int generateRandomNumber(int min, int max) {
    return min + (Random().nextInt(max - min + 1));
  }

  // Add more utility methods as needed...
}
    