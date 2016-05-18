package de.maefvfis.gameoverlay.objects;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;


public class WireSphere {
	
	public static int detail = 64;
	
	public static int sphereIdOutside;

	public static void initSphere () {
		

		
		Sphere sphere = new Sphere();
		//GLU_FILL as a solid.
		sphere.setDrawStyle(GLU.GLU_LINE);
		//GLU_SMOOTH will try to smoothly apply lighting
		sphere.setNormals(GLU.GLU_SMOOTH);
		sphere.setOrientation(GLU.GLU_OUTSIDE);
		sphereIdOutside = GL11.glGenLists(1);
		GL11.glNewList(sphereIdOutside, GL11.GL_COMPILE);

		//The drawing the sphere is automatically doing is getting added to our list. Careful, the last 2 variables
		//control the detail, but have a massive impact on performance. 32x32 is a good balance on my machine.s
		sphere.draw(16f, detail, detail);
		GL11.glEndList();
	}
}
