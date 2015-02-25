package edu.byuh.cis.main;

import java.util.ArrayList;
import java.util.List;

import android.graphics.PointF;
import android.graphics.RectF;

public class HelperMethods {
	
	public Person containPoint(ArrayList<Person> family, PointF p) {
		
		for (Person pe : family) {
			if (pe.getBounds().contains(p.x,p.y)) {
				return pe;
			}
		}
		return null;
	}
	public void resetPositions(Person p) {
		if (p.hasParents()) {
			resetPositions(p.getFather());
			resetPositions(p.getMother());
		} 
		p.setPosition(null);
		p.setBounds(new RectF());
		
	}
	public void setPositions(int range, Person root, float screenWidth, float screenHeight, float margin) {
		switch(range) {
			case 4: root.getFather().getFather().getFather().setPosition(new PointF((float) (screenWidth/2+margin*7/2*Math.cos(Math.PI*13/8)), (float) (screenHeight/2+margin*7/2*Math.sin(Math.PI*13/8))));
					root.getFather().getFather().getMother().setPosition(new PointF((float) (screenWidth/2+margin*7/2*Math.cos(Math.PI*15/8)), (float) (screenHeight/2+margin*7/2*Math.sin(Math.PI*15/8))));
					root.getFather().getMother().getFather().setPosition(new PointF((float) (screenWidth/2+margin*7/2*Math.cos(Math.PI/8)), (float) (screenHeight/2+margin*7/2*Math.sin(Math.PI/8))));
					root.getFather().getMother().getMother().setPosition(new PointF((float) (screenWidth/2+margin*7/2*Math.cos(Math.PI*3/8)), (float) (screenHeight/2+margin*7/2*Math.sin(Math.PI*3/8))));
					root.getMother().getFather().getFather().setPosition(new PointF((float) (screenWidth/2+margin*7/2*Math.cos(Math.PI*11/8)), (float) (screenHeight/2+margin*7/2*Math.sin(Math.PI*11/8))));
					root.getMother().getFather().getMother().setPosition(new PointF((float) (screenWidth/2+margin*7/2*Math.cos(Math.PI*9/8)), (float) (screenHeight/2+margin*7/2*Math.sin(Math.PI*9/8))));
					root.getMother().getMother().getFather().setPosition(new PointF((float) (screenWidth/2+margin*7/2*Math.cos(Math.PI*7/8)), (float) (screenHeight/2+margin*7/2*Math.sin(Math.PI*7/8))));
					root.getMother().getMother().getMother().setPosition(new PointF((float) (screenWidth/2+margin*7/2*Math.cos(Math.PI*5/8)), (float) (screenHeight/2+margin*7/2*Math.sin(Math.PI*5/8))));
					
			case 3: root.getFather().getFather().setPosition(new PointF((float) (screenWidth/2+margin*5/2*Math.cos(Math.PI*7/4)), (float) (screenHeight/2 +margin*5/2*Math.sin(Math.PI*7/4))));
					root.getFather().getMother().setPosition(new PointF((float) (screenWidth/2+margin*5/2*Math.cos(Math.PI/4)), (float) (screenHeight/2 +margin*5/2*Math.sin(Math.PI/4))));
					root.getMother().getFather().setPosition(new PointF((float) (screenWidth/2+margin*5/2*Math.cos(Math.PI*5/4)), (float) (screenHeight/2 +margin*5/2*Math.sin(Math.PI*5/4))));
					root.getMother().getMother().setPosition(new PointF((float) (screenWidth/2+margin*5/2*Math.cos(Math.PI*3/4)), (float) (screenHeight/2 +margin*5/2*Math.sin(Math.PI*3/4))));
			case 2: root.getFather().setPosition(new PointF(screenWidth/2+margin*3/2, screenHeight/2));
					root.getMother().setPosition(new PointF(screenWidth/2-margin*3/2, screenHeight/2));
			default: root.setPosition(new PointF(screenWidth/2, screenHeight/2));
		}
	}
	
	public int getGeneration(Person p, int i) {
		if (p.hasParents()) {
			return getGeneration(p.getParents(), ++i);
		} else {
			return i;
		}
	}
	public void buildTree(List<Person> family, int i) {
		if(i*2+1 < family.size()) {
			family.get(i).setParents(family.get(i*2+1), family.get(i*2+2));
			buildTree(family, i*2+1);
			buildTree(family, i*2+2);
		} else {
			return;
		}
	}
}
