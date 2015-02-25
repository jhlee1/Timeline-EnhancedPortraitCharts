package edu.byuh.cis.main;
import java.util.HashMap;
import java.util.Map.Entry;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;


public class Person {
	private String name;
	private String birthdate;
	private String deathdate;
	private Person mother;
	private Person father;
	private PointF position;
	private HashMap<Integer, Bitmap> pictures = new HashMap<>();
	private Bitmap picture;
	private Bitmap tempPic;
	private RectF bounds;
	
	public Person(String name) {
		this.name = name;
		mother = null;
		father = null;
		bounds = new RectF();
		birthdate = "";
		deathdate = "";
	}

	
	public Person(String name, Resources res) {
		this(name);
	}
	public Person(String name, Resources res, String birth, String death) {
		this(name);
		birthdate = birth;
		deathdate = death;
	}
	

	
	public Person setName(String name) {
		this.name = name;
		 
		return this;
	}
	
	public Person setBirthdate(String bd) {
		birthdate = bd;
		 
		return this;
	}
	
	public String getBirthdate() {
		return birthdate;
	}
	
	public Person setMother(Person m) {
		mother = m;
		 
		return this;
	}
	public String getName() {
		return name;
	}
	
	public Person setFather(Person f) {
		father = f;
		return this;
	}
	
	public void setParents(Person f, Person m) {
		setMother(m);
		setFather(f);
		//no need to recomputeID since the above methods do it.
	}
	
	public Person getMother() {
		return mother;
	}

	public Person getFather() {
		return father;
	}
	
	public HashMap<Integer, Bitmap> getPictures() {
		return pictures;
	}

	public void setPictures(HashMap<Integer, Bitmap> pictures) {
		this.pictures = pictures;
		// this.picture = the earliest picture among pictures
	}

	public PointF getPosition() {
		return position;
	}

	public void setPosition(PointF position) {
		this.position = position;
	}
	public boolean hasParents () {
		if (father != null || mother != null) {
			return true;
		} else {
			return false;
		}
	}
	public Person getParents () {
		if (this.father != null) {
			return this.father;
		} else if (mother != null) {
			return this.mother;
		} else { 
			return null;
		}
	}

	public Bitmap getTempPic() {
		return tempPic;
	}

	public void setTempPic(Bitmap tempPic) {
		this.tempPic = tempPic;
	}
	public Bitmap getPicture() {	
		return this.picture;
	}
	public Bitmap getPicture(int timePeriod) {
		int key = 0;
		int min = 99999;
		if (Integer.parseInt(this.getDeathdate()) <= timePeriod && this.pictures.get(99999) != null ) {
			return this.pictures.get(99999);
		}
		
		for (Entry<Integer,Bitmap> e : pictures.entrySet()) {
			int gap = timePeriod - e.getKey() ;
			if ( gap > 0 && gap < min) {
				min = gap;
				key = e.getKey();
			}
		}
		if (key == 0) {
			min = 999999;
			for (Entry<Integer,Bitmap> e : pictures.entrySet()) {
				if (e.getKey() < min) {
					min = e.getKey();
				}
			}
			return this.pictures.get(min);
		}
		
		return this.pictures.get(key);
	}

	public void setPicture(Bitmap picture) {
		this.picture = picture;
	}

	public RectF getBounds() {
		return bounds;
	}

	public void setBounds(RectF bounds) {
		this.bounds = bounds;
	}
	public String getDeathdate() {
		return deathdate;
	}
	public void setDeathdate(String deathdate) {
		this.deathdate = deathdate;
	}


}