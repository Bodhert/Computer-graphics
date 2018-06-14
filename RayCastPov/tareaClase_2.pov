#include "colors.inc"

                  
plane 
{
 y,-2
 pigment{checker Pink, White}

}                  
                  
sphere{ <0,0,0>, 0.3
 texture{ 
    pigment{ 
        color rgb<1, 1,1>} 
        normal{ 
                leopard -5 scale 0.015}
                finish { diffuse 0.9 phong 0.1}
              } // end of texture 1
              
        texture{ 
            pigment{
                 image_map
                 { 
                    gif "jupiter.gif"
                    map_type 0
                    once
                    transmit 200, 1
                 }  
                 
                 translate<-0.5,-0.5,0>
                 scale<0.75,1,1>*0.85
                 
                 } // end pigment
                 
               } // end of texture 2       
} // end of sphere  ------------------
  
  
  
  
  
  
  camera 
{
 location<0,0,-1>
 look_at<0,0,0>
}


light_source
{
 <10,10,-10>
 color White
}   