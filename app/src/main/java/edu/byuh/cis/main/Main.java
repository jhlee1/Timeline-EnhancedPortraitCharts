package edu.byuh.cis.main;
import edu.byuh.cis.main.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class Main extends Activity {
	
	private MainView mv;
	private SeekBar skb;
	private TextView tvBirth;
	private TextView tvDeath;
	private TextView tvYear; 
	/**
	 * Recursive functions should be changed to literal using family list ...
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acti);

		
		skb = (SeekBar) findViewById(R.id.timeChooser);
		mv = (MainView) findViewById(R.id.mainView);
		tvBirth = (TextView) findViewById(R.id.birth);
		tvBirth.setText(mv.getRoot().getBirthdate());
		tvDeath = (TextView) findViewById(R.id.death);
		tvDeath.setText(mv.getRoot().getDeathdate());
		tvYear = (TextView) findViewById(R.id.year);
		tvYear.setText(mv.getRoot().getBirthdate());
		
		skb.setMax(Integer.parseInt(mv.getRoot().getDeathdate())-Integer.parseInt(mv.getRoot().getBirthdate()));
		skb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				mv.setTimePoint(progress);
				tvYear.setText(Integer.parseInt(mv.getRoot().getBirthdate())+ skb.getProgress() + "");
				mv.invalidate();
				
			}
		});

		/**
		 * Need to implement rotation feature later
		 * Display display = ((WindowManager) mv.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		 */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		
		int id = item.getItemId();
		if (id == R.id.action_about) {
			alertDialogBuilder.setTitle("About Timeline-Enhanced Portrait Charts");
			alertDialogBuilder.setMessage("Programming by Joohan Lee under the supervision of Dr. Geoffrey M. Draper. This app is a research project funded by Brigham Young University Hawaii.");
			alertDialogBuilder.setPositiveButton("OK", null);
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
