package edu.byuh.cis.main;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import edu.byuh.cis.main.R;

public class MainView extends View {
	private int range;
	private Paint firstLayer = new Paint();
	private Paint secondLayer = new Paint();
	private Paint thirdLayer = new Paint();
	private Paint fourthLayer = new Paint();
	private Paint white = new Paint();
	private Paint black = new Paint();
	private float screenWidth;
	private float screenHeight;
	private float radius;
	private float margin;
	private Person root ;
	private float x1= 0,y1 = 0,x2 = 0,y2 = 0 ;
	private Resources res;
	private int timePoint;
	private HelperMethods hm;
	private Person moving;
	private int seekBarMax;
	/**
	 * Try using list
	 */
	ArrayList<Person> family = new ArrayList<>();
	
	/**
	 * Here I delare people 
	 * these are supposed to be deleted when I use list instead.
	 * @param context
	 */
	private Person me;
	private Person father;
	private Person mother;
	private Person grandFather1;
	private Person grandMother1;
	private Person grandFather2;
	private Person grandMother2;
	private Person greatGrandFather1;
	private Person greatGrandMother1;
	private Person greatGrandFather2;
	private Person greatGrandMother2;
	private Person greatGrandFather3;
	private Person greatGrandMother3;
	private Person greatGrandFather4;
	private Person greatGrandMother4;
	private boolean init;
	private Activity mainActivity;
	private TextView tvBirth;
	private TextView tvDeath;
	private SeekBar skb ;
	private AlertDialog.Builder alertDialogBuilder;
	
	
	
	public MainView (Context context, AttributeSet abs) {
		super(context, abs);
		hm = new HelperMethods();
		setColors();
		res = getResources();
		instantiatePeople();
		root = family.get(0);
		setTimePoint(0);
		init = true;
		range = (hm.getGeneration(root, 1) > 4)? 4 : hm.getGeneration(root, 1);
		alertDialogBuilder = new AlertDialog.Builder(getContext());
		
	}

	private void setColors() {
		black.setColor(Color.BLACK);
		black.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD));
		white.setColor(Color.WHITE);
		white.setStrokeWidth(4);
		fourthLayer.setColor(Color.rgb(229,180,137));
		thirdLayer.setColor(Color.rgb(239,212,178));
		secondLayer.setColor(Color.rgb(230,226,214));
		firstLayer.setColor(Color.rgb(229,245,237));
	}

	@Override
	public void onDraw(Canvas c) {
		super.onDraw(c);
		if (init) {
			screenWidth = c.getWidth();
			screenHeight = c.getHeight();
			if (screenHeight < screenWidth) {
				screenWidth = screenWidth*3/6;
				radius = (screenHeight < screenWidth) ? screenHeight/2 : screenWidth/2;
			} else { 
				screenHeight = screenHeight*3/4;
				radius = (screenHeight < screenWidth) ? screenHeight/2 : screenWidth/2;
			}
			margin = radius / range;
			black.setTextSize(margin/8.5f);
			hm.setPositions(range,root,screenWidth,screenHeight,margin);
			changePhotoSize(root);
			init = false;
		}
		c.drawColor(Color.rgb(222, 222, 222));
		drawLayers(c);
		drawNames(c,root);
	}
	
	public void drawLayers (Canvas c) {
		switch (range) {
			case 4:	c.drawCircle(screenWidth/2, screenHeight/2, margin*4+4, white); 
					c.drawCircle(screenWidth/2, screenHeight/2, margin*4, fourthLayer);
					c.drawLine((float)(screenWidth/2 - margin*4*Math.cos(Math.PI/4)), 
							(float)(screenHeight/2 - margin*4*Math.sin(Math.PI/4)),
							(float)(screenWidth/2 + margin*4*Math.cos(Math.PI/4)),
							(float)(screenHeight/2 + margin*4*Math.sin(Math.PI/4)),white);
					c.drawLine((float)(screenWidth/2 + margin*4*Math.cos(Math.PI/4)), 
							(float)(screenHeight/2 - margin*4*Math.sin(Math.PI/4)),
							(float)(screenWidth/2 - margin*4*Math.cos(Math.PI/4)),
							(float)(screenHeight/2 + margin*4*Math.sin(Math.PI/4)),white);
 			case 3: c.drawCircle(screenWidth/2, screenHeight/2, margin*3+4, white);
 					c.drawCircle(screenWidth/2, screenHeight/2, margin*3, thirdLayer);
					c.drawLine(screenWidth/2-radius, screenHeight/2,screenWidth/2+radius , screenHeight/2, white);
			case 2:	c.drawCircle(screenWidth/2, screenHeight/2, margin*2+4, white);
					c.drawCircle(screenWidth/2, screenHeight/2, margin*2, secondLayer);
					c.drawLine(screenWidth/2, screenHeight/2-radius, screenWidth/2, screenHeight/2+radius, white);
			default:c.drawCircle(screenWidth/2, screenHeight/2, margin+4, white);
					c.drawCircle(screenWidth/2, screenHeight/2, margin, firstLayer);
		}
	}
	
	
	public void drawNames (Canvas c, Person p) {
		if (p.hasParents()) {
			drawNames(c, p.getFather());
			drawNames(c, p.getMother());
		}
		if (p.getPosition() != null) {
			c.drawText(p.getName(), p.getBounds().left - p.getName().length()*radius/250f, p.getBounds().bottom+black.getTextSize(), black);
		}
		if (p.getPosition() != null && p.getTempPic() != null) {
			c.drawBitmap(p.getTempPic(),p.getBounds().centerX() - p.getTempPic().getWidth()/2, p.getBounds().centerY() - p.getTempPic().getHeight()/2, white );
		}			
	}
	public void changePhotoSize (Person p) {
		if (p.hasParents()) {
			changePhotoSize(p.getFather());
			changePhotoSize(p.getMother());
		}
		
		if (p.getPicture(timePoint) != null) {
			if (p.equals(me)) {
				p.setTempPic(Bitmap.createScaledBitmap(p.getPicture(timePoint),(int) (radius/(1.5*range)), (int) (radius/(1.3*range)), true));		
			} else {
				p.setTempPic(Bitmap.createScaledBitmap(p.getPicture(timePoint),(int) radius/(2*range), (int) (radius/(1.3*range)), true));
			}
			if (p.getPosition() != null)
				p.setBounds(new RectF(p.getPosition().x - p.getTempPic().getWidth()/2, p.getPosition().y - p.getTempPic().getHeight()/2, p.getPosition().x + p.getTempPic().getWidth()/2, p.getPosition().y + p.getTempPic().getHeight()/2));
		} else {
			Log.d("NULL", "get picture is null");
		}

	}


	@Override
	public boolean onTouchEvent(MotionEvent m) {
		if (m.getAction() == MotionEvent.ACTION_DOWN) {
			//highlight the duck if the user tapped it
			x1 = m.getX();
			y1 = m.getY();
			moving = hm.containPoint(family, new PointF(x1,y1));
			if (moving != null) {
				if (moving.equals(root)) {
					moving = null;
				}
			}
			invalidate();
			return true;
		}
		if (m.getAction() == MotionEvent.ACTION_MOVE) {
			if (moving != null) {
				moving.getBounds().offsetTo(m.getX()-moving.getBounds().width()/2 , m.getY()-moving.getBounds().height()/2);				
				invalidate();
				return true;
			}
		}
		if (m.getAction() == MotionEvent.ACTION_UP) {
			x2 = m.getX();
			y2 = m.getY();
			if (Math.abs(x2-x1)+Math.abs(y2-y1) < 10 && hm.containPoint(family, new PointF(x2,y2)) != null) {
				Person p = hm.containPoint(family, new PointF(x2,y2));
					alertDialogBuilder.setTitle(p.getName());
					alertDialogBuilder.setMessage("Birth Date:" + p.getBirthdate() +"\n"
							+"Death Date:" + p.getDeathdate() + "\n");
					alertDialogBuilder.setPositiveButton("OK", null);
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
					return false;
			}
			if (moving != null && root.getBounds().intersect(moving.getBounds())) {
				root = moving;
				range = (hm.getGeneration(root, 1) > 4)? 4 : hm.getGeneration(root, 1);
				hm.resetPositions(root);
				resetBar();
				init = true;
				setTimePoint(0);
				invalidate();
				moving = null;
				return false;
			} else if (moving != null) {
				moving.setBounds(new RectF(moving.getPosition().x - moving.getTempPic().getWidth()/2, moving.getPosition().y - moving.getTempPic().getHeight()/2, moving.getPosition().x + moving.getTempPic().getWidth()/2, moving.getPosition().y + moving.getTempPic().getHeight()/2));

			}
			if (Math.abs(y2-y1) > 30  && Math.abs(x1 - x2) < 80 && moving == null) {
				if (y2>y1) {
					y1 = 0;
					y2 = 0;
					if (range < hm.getGeneration(root, 1) && range >= 1) {
						range++;
						setTimePoint(0);
						init = true;
						hm.resetPositions(root);
						return false;
					}
				}
				if (y2<y1) {
					y1 = 0;
					y2 = 0;					
					if (range > 1) {
						range--;
						setTimePoint(0);
						init = true;
						hm.resetPositions(root);
					}

				}
			}
			if (x2<x1 &&Math.abs(x2-x1) > 30  && Math.abs(y1 - y2) < 80 && moving == null && range < 4 && family.indexOf(root)!= 0) {
				root = family.get((family.indexOf(root)-1)/2);
				range = hm.getGeneration(root, 1);
				resetBar();
				init = true;
				setTimePoint(0);
				hm.resetPositions(root);
				return false;
			}
			
		}
		invalidate();
		return false;

	}
	private void instantiatePeople () {
		HashMap<Integer, Bitmap> pics;

		//Instantiate people
		me = new Person("David Draper", res, "1947" , "1999" );
		pics = new HashMap<>();
		pics.put(1947, BitmapFactory.decodeResource(res, R.drawable.david1947));
		pics.put(1948, BitmapFactory.decodeResource(res, R.drawable.david1948));
		pics.put(1949, BitmapFactory.decodeResource(res, R.drawable.david1949));
		pics.put(1950, BitmapFactory.decodeResource(res, R.drawable.david1950));
		pics.put(1957, BitmapFactory.decodeResource(res, R.drawable.david1957));
		pics.put(1958, BitmapFactory.decodeResource(res, R.drawable.david1958));
		pics.put(1961, BitmapFactory.decodeResource(res, R.drawable.david1961));
		pics.put(1965, BitmapFactory.decodeResource(res, R.drawable.david1965));
		pics.put(1968, BitmapFactory.decodeResource(res, R.drawable.david1968));
		pics.put(1969, BitmapFactory.decodeResource(res, R.drawable.david1969));
		pics.put(1970, BitmapFactory.decodeResource(res, R.drawable.david1970));
		pics.put(1971, BitmapFactory.decodeResource(res, R.drawable.david1971));
		pics.put(1979, BitmapFactory.decodeResource(res, R.drawable.david1979));
		pics.put(1980, BitmapFactory.decodeResource(res, R.drawable.david1980));
		pics.put(1986, BitmapFactory.decodeResource(res, R.drawable.david1986));
		pics.put(1987, BitmapFactory.decodeResource(res, R.drawable.david1987));
		pics.put(1994, BitmapFactory.decodeResource(res, R.drawable.david1994));
		pics.put(1996, BitmapFactory.decodeResource(res, R.drawable.david1996));
		pics.put(99999, BitmapFactory.decodeResource(res, R.drawable.grave_david));		
		me.setPictures(pics);
		family.add(me);
		
		father = new Person("Rulon Draper", res, "1917" , "1997");
		pics = new HashMap<>();
		pics.put(1917, BitmapFactory.decodeResource(res, R.drawable.rulon1917));
		pics.put(1918, BitmapFactory.decodeResource(res, R.drawable.rulon1918));
		pics.put(1945, BitmapFactory.decodeResource(res, R.drawable.rulon1945));
		pics.put(1947, BitmapFactory.decodeResource(res, R.drawable.rulon1947));
		pics.put(1950, BitmapFactory.decodeResource(res, R.drawable.rulon1950));
		pics.put(1965, BitmapFactory.decodeResource(res, R.drawable.rulon1965));
		pics.put(1968, BitmapFactory.decodeResource(res, R.drawable.rulon1968));
		pics.put(1970, BitmapFactory.decodeResource(res, R.drawable.rulon1970));
		pics.put(1985, BitmapFactory.decodeResource(res, R.drawable.rulon1985));
		pics.put(1986, BitmapFactory.decodeResource(res, R.drawable.rulon1986));
		pics.put(1996, BitmapFactory.decodeResource(res, R.drawable.rulon1996));
		pics.put(99999, BitmapFactory.decodeResource(res, R.drawable.grave_rulon_and_donna));
		father.setPictures(pics);
		family.add(father);
		
		mother = new Person("Donna Widdison",res, "1924" , "2011");
		pics = new HashMap<>();
		pics.put(1932, BitmapFactory.decodeResource(res, R.drawable.donna1932));
		pics.put(1942, BitmapFactory.decodeResource(res, R.drawable.donna1942));
		pics.put(1945, BitmapFactory.decodeResource(res, R.drawable.donna1945));
		pics.put(1947, BitmapFactory.decodeResource(res, R.drawable.donna1947));
		pics.put(1949, BitmapFactory.decodeResource(res, R.drawable.donna1949));
		pics.put(1950, BitmapFactory.decodeResource(res, R.drawable.donna1950));
		pics.put(1965, BitmapFactory.decodeResource(res, R.drawable.donna1965));
		pics.put(1968, BitmapFactory.decodeResource(res, R.drawable.donna1968));
		pics.put(1970, BitmapFactory.decodeResource(res, R.drawable.donna1970));
		pics.put(1986, BitmapFactory.decodeResource(res, R.drawable.donna1986));
		pics.put(1996, BitmapFactory.decodeResource(res, R.drawable.donna1996));
		pics.put(99999, BitmapFactory.decodeResource(res, R.drawable.grave_rulon_and_donna));
		mother.setPictures(pics);
		family.add(mother);
		
		grandFather1 = new Person("Terry Draper", res, "1893" , "1976");
		pics = new HashMap<>();
		pics.put(1913, BitmapFactory.decodeResource(res, R.drawable.terry1913));
		pics.put(1919, BitmapFactory.decodeResource(res, R.drawable.terry1919));
		pics.put(1965, BitmapFactory.decodeResource(res, R.drawable.terry1965));
		pics.put(1975, BitmapFactory.decodeResource(res, R.drawable.terry1975));
		pics.put(99999, BitmapFactory.decodeResource(res, R.drawable.grave_terry));
		grandFather1.setPictures(pics);
		family.add(grandFather1);
		
		grandMother1 = new Person("Florence Beckham", res, "1894" , "1934");
		pics = new HashMap<>();
		pics.put(1902, BitmapFactory.decodeResource(res, R.drawable.florence1902));
		pics.put(1912, BitmapFactory.decodeResource(res, R.drawable.florence1912));
		pics.put(1917, BitmapFactory.decodeResource(res, R.drawable.florence1917));
		pics.put(1919, BitmapFactory.decodeResource(res, R.drawable.florence1919));
		pics.put(1925, BitmapFactory.decodeResource(res, R.drawable.florence1925));
		pics.put(1929, BitmapFactory.decodeResource(res, R.drawable.florence1929));
		pics.put(99999, BitmapFactory.decodeResource(res, R.drawable.grave_florence));
		grandMother1.setPictures(pics);
		family.add(grandMother1);
		
		grandFather2 = new Person ("James Widdison", res, "1879" , "1938");
		pics = new HashMap<>();
		pics.put(1903, BitmapFactory.decodeResource(res, R.drawable.james1903));
		pics.put(1914, BitmapFactory.decodeResource(res, R.drawable.james1914));
		pics.put(1932, BitmapFactory.decodeResource(res, R.drawable.james1932));
		pics.put(1938, BitmapFactory.decodeResource(res, R.drawable.james1938));
		pics.put(99999, BitmapFactory.decodeResource(res, R.drawable.grave_james_jr_and_lillian));
		grandFather2.setPictures(pics);
		family.add(grandFather2);
		
		grandMother2 = new Person("Lillian Gardner", res, "1883" , "1972");
		pics = new HashMap<>();
		pics.put(1932, BitmapFactory.decodeResource(res, R.drawable.lillian1932));
		pics.put(1938, BitmapFactory.decodeResource(res, R.drawable.lillian1938));
		pics.put(1948, BitmapFactory.decodeResource(res, R.drawable.lillian1948));
		pics.put(99999, BitmapFactory.decodeResource(res, R.drawable.grave_james_jr_and_lillian));
		grandMother2.setPictures(pics);
		family.add(grandMother2);
		
		
		greatGrandFather1 = new Person("Zemira Draper", res, "1859","1907");
		pics = new HashMap<>();
		pics.put(1890, BitmapFactory.decodeResource(res, R.drawable.zemira1890));
		pics.put(99999, BitmapFactory.decodeResource(res, R.drawable.grave_zemira));
		greatGrandFather1.setPictures(pics);
		family.add(greatGrandFather1);
		
		greatGrandMother1 = new Person("Olga Poulson", res, "1869","1948");
		pics = new HashMap<>();
		pics.put(1900, BitmapFactory.decodeResource(res, R.drawable.olgapoulson1900));
		pics.put(1930, BitmapFactory.decodeResource(res, R.drawable.olgapoulson1930));
		pics.put(1945, BitmapFactory.decodeResource(res, R.drawable.olgapoulson1945));
		pics.put(99999, BitmapFactory.decodeResource(res, R.drawable.grave_olga));
		greatGrandMother1.setPictures(pics);
		family.add(greatGrandMother1);
	
		
		greatGrandFather2 = new Person("Samuel Becham", res, "1858","1991");
		pics = new HashMap<>();
		pics.put(1880, BitmapFactory.decodeResource(res, R.drawable.samuelbeckham1880));
		pics.put(1890, BitmapFactory.decodeResource(res, R.drawable.samuelbeckham1890));
		pics.put(99999, BitmapFactory.decodeResource(res, R.drawable.grave_samuel));
		greatGrandFather2.setPictures(pics);
		family.add(greatGrandFather2);
		
		greatGrandMother2 = new Person("Esther Ransom", res, "1848","1911");
		pics = new HashMap<>();
		pics.put(1870, BitmapFactory.decodeResource(res, R.drawable.estherransom1870));
		pics.put(1880, BitmapFactory.decodeResource(res, R.drawable.estherransom1880));
		greatGrandMother2.setPictures(pics);
		family.add(greatGrandMother2);
		
		greatGrandFather3 = new Person("James Widdison, Sr", res, "1853","1923");
		pics = new HashMap<>();
		pics.put(1920, BitmapFactory.decodeResource(res, R.drawable.jameswiddisonsr1920));
		pics.put(99999, BitmapFactory.decodeResource(res, R.drawable.grave_james_sr_and_alice));
		greatGrandFather3.setPictures(pics);
		family.add(greatGrandFather3);
		
		greatGrandMother3 = new Person("Alice Pinney", res, "1860", "1915");
		pics = new HashMap<>();
		pics.put(1910, BitmapFactory.decodeResource(res, R.drawable.alicepinney1910));
		pics.put(99999, BitmapFactory.decodeResource(res, R.drawable.grave_james_sr_and_alice));
		greatGrandMother3.setPictures(pics);
		family.add(greatGrandMother3);
		
		greatGrandFather4 = new Person ("Alchibald Gardner", res, "1814","1902");
		pics = new HashMap<>();
		pics.put(1840, BitmapFactory.decodeResource(res, R.drawable.archibald1840));
		pics.put(1880, BitmapFactory.decodeResource(res, R.drawable.archibald1880));
		pics.put(99999, BitmapFactory.decodeResource(res, R.drawable.grave_archibald_and_marie_hansen));
		greatGrandFather4.setPictures(pics);
		family.add(greatGrandFather4);
		
		greatGrandMother4 = new Person("Marie Hansen", res, "1850","1921");
		pics = new HashMap<>();
		pics.put(1875, BitmapFactory.decodeResource(res, R.drawable.mariehansen1875));
		pics.put(1900, BitmapFactory.decodeResource(res, R.drawable.mariehansen1900));
		pics.put(99999, BitmapFactory.decodeResource(res, R.drawable.grave_archibald_and_marie_hansen));
		greatGrandMother4.setPictures(pics);
		family.add(greatGrandMother4);
		hm.buildTree(family, 0);	
	}

	public int getTimePoint() {
		return timePoint;
	}

	public void setTimePoint(int timePoint) {
		this.timePoint = timePoint +Integer.parseInt(root.getBirthdate());
		this.init = true;
	}

	public int getSeekBarMax() {
		return seekBarMax;
	}

	public void setSeekBarMax(int seekBarMax) {
		this.seekBarMax = seekBarMax;
	}
	public Person getRoot() {
		return root;
		
	}
	public void resetBar() {
		mainActivity = (Activity) getContext();
		tvBirth = (TextView) mainActivity.findViewById(R.id.birth);
		tvDeath = (TextView) mainActivity.findViewById(R.id.death);
		skb = (SeekBar) mainActivity.findViewById(R.id.timeChooser);
		tvBirth.setText(root.getBirthdate());
		tvDeath.setText(root.getDeathdate());
		skb.setMax(Integer.parseInt(root.getDeathdate())-Integer.parseInt(root.getBirthdate()));
		skb.setProgress(0);
	}
	
}
