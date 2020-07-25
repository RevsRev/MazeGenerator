public class ArrayMethods {
  public int[] addVectors(int[] vec1, int[] vec2) {
    if (vec1.length != vec2.length) {
      System.out.println("Error! Two Vectors were not the same length");
      System.exit(0);
    }

    int[] answer = new int[vec1.length];

    for (int i=0; i<vec1.length; i++) {
      answer[i] = vec1[i] + vec2[i];
    }
    return answer;
  }

  public int[] removeElement(int index, int[] vec) {
    if (index<0 || index>= vec.length) {
      System.out.println("Error! Array index was out of range");
      System.exit(0);
    }

    if (vec.length == 1) {
      return null;
    }

    int[] answer = new int[vec.length -1];

    for (int i = 0; i< answer.length; i++) {
      if (i<index) {
        answer[i] = vec[i];
      } else if (i == index) {
        continue;
      } else {
        answer[i] = vec[i+1];
      }
    }

    return answer;
  }

  public int[][] removeElement(int index, int[][] array) {
    if (index<0 || index>= array.length) {
      System.out.println("Error! Array index was out of range");
      System.exit(0);
    }

    int[][] answer = new int[array.length -1][array[0].length]; //only works for rectangular arrays!

    for (int i = 0; i< answer.length; i++) {
      if (i<index) {
        for (int j=0; j<array[i].length; j++) {
          answer[i][j] = array[i][j];
        }
      } else if (i >= index) {
        for (int j=0; j<array[i].length; j++) {
          answer[i][j] = array[i+1][j];
        }
      }
    }

    return answer;
  }

  public int[][] Append(int[] newElement, int[][] array) {
    int [][] answer = new int[array.length+1][];
    for (int i=0; i<array.length; i++) {
      answer[i] = array[i];
    }
    answer[array.length] = newElement;
    return answer;
  }

  public void printArray(boolean[][] array) {
    for (int i=0; i<array.length; i++) {
      for (int j=0; j<array[i].length; j++) {
        System.out.print(array[i][j] + " , ");
      }
      System.out.println("\n");
    }
  }

  public void printArray(int[][] array) {
    for (int i=0; i<array.length; i++) {
      for (int j=0; j<array[i].length; j++) {
        System.out.print(array[i][j] + " , ");
      }
      System.out.println("\n");
    }
  }
}
