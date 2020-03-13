public class Graph {

  City cities[];
  float distances[][];
  int size;
  int curSize;

  public Graph (int n) {
    size = n;
    curSize = 0;
    cities = new City[n];
    distances = new float[n][n];

  }

  void calculateMatrix () {
    for (int i = 0; i < curSize; i++) {
      for (int j = i + 1; j < curSize; j++) {
        float temp = (float)Math.pow(cities[i].xPos - cities[j].xPos, 2) + (float)Math.pow(cities[i].yPos - cities[j].yPos, 2);
        distances[i][j] = temp;
        distances[j][i] = temp;
      }
    }
  }

  void addCity (int x, int y) {
    if (curSize == size) {
      System.out.println("City @ (" + x + ", " + y + ") not added.");
      return;
    }
    cities[curSize++] = new City(x, y);
  }

  void test_print () {
    for (int i = 0; i < curSize; i++) {
      for (int j = 0; j < curSize; j++) {
        System.out.print((int)distances[i][j] + " ");
      }
      System.out.print("\n");
    }
  }

}
