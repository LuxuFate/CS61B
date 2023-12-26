public class Code {

  public static void main(String[] args){


  }

  public static int max(int[] a){
    int max = a[0];
    for(int check = 1; check < a.length; check++){
      if (a[check]>max){
        max = a[check];
      }
    }
    return max;
  }

  public static boolean threeSum(int[] a){
    for(int x = 0; x < a.length; x++){
      for(int y = 0; y < a.length; y++){
        for(int z = 0; z < a.length; z++){
          if((a[x]+a[y]+a[z]) == 0){
            return true;
          }
        }
      }
    }
    return false;
  }

  public static boolean threeSumDistinct(int[] a){
    for(int x = 0; x < a.length; x++){
      for(int y = 0; y < a.length; y++){
        for(int z = 0; z < a.length; z++){
          if((x==y)||(y==z)||(z==x)){
            break;
          }
          else {
            if((a[x]+a[y]+a[z]) == 0){
              return true;
            }
          }
        }
      }
    }
    return false;
  }




}
