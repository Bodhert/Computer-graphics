#include "colors.inc"

plane 
{
 y,-2
 pigment{checker Pink, White}

}


#local ambiente_1 = 0;
#local diffuse_1 = 0;

#for (_x, -10, 0, 3)
 #for (_y, 0, 10, 3)

     sphere
        {
        <_x,_y,10>, 1
        pigment { Green}
        finish
         {
            ambient ambiente_1
            diffuse diffuse_1
         }
        } 
        #local ambiente_1 = ambiente_1 + 0.1;
    #end  
    #local diffuse_1 = diffuse_1 + 0.1;      
    #local ambiente_1 = 0;                             
      
 #end // ----------- end of #for loop




#local specular_ = 0;
#local diffuse_2 = 0;

#for (_x, 2, 12, 3)
 #for (_y, 0, 10, 3)

     sphere
        {
        <_x,_y,10>, 1
        pigment { Red}
        finish
         {
            phong specular_
            diffuse diffuse_1
            phong_size 10
         }
        } 
        #local diffuse_2 = diffuse_2 + 0.1;
    #end  
    #local specular_ = specular_ + 0.1;    
    #local diffuse_2 = 0;                               
      
 #end // ----------- end of #for loop


light_source
{
 <10,10,-10>
 color White
}           


camera 
{
 location<0,0,-15>
 look_at<0,0,0>
}
