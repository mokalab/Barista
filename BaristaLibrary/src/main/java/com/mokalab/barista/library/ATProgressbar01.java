package com.mokalab.barista.library;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

//import com.antoniotari.android.util.ATUtil;


public class ATProgressbar01 extends LinearLayout implements Runnable {

	//private Context context;
	private ArrayList<ImageView> imageHolders;
	private ArrayList<String> images;
	private Thread animationThread;
	private boolean stopped = true;
	private static int[] _colors;

	public ATProgressbar01(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		prepareLayout(true,0);
	}

	public ATProgressbar01(Context context) 
	{
		this(context,true);
	}

	public ATProgressbar01(Context context,boolean fillWidth)
	{
		super(context);
		//this.context = context;
		prepareLayout(fillWidth,0);
	}
	
	public ATProgressbar01(Context context,boolean fillWidth,int numViews)
	{
		super(context);
		prepareLayout(fillWidth,numViews);
	}

	public void setColors(int[] colors)
	{
		if(colors==null)
			return;
		
		if(imageHolders!=null)
		{
			for(ImageView imv:imageHolders)
			{
				
				//imageHolders.remove(imv);
			}
		}
		
		_colors=colors;
		TableLayout.LayoutParams lParam=new TableLayout.LayoutParams(0,LayoutParams.MATCH_PARENT, 10f);
		
		for(int i=0;i<_colors.length;i++)
		{
			
			ImageView imV=new ImageView(getContext());			
			imV.setLayoutParams(lParam);
			imV.setTag(imageHolders.size()+1);
			imV.setImageDrawable(new ColorDrawable(Color.YELLOW));
			this.addView(imV);
			imageHolders.add(imV);
		}
	}
	
	/**
	 * This is called when you want the dialog to be dismissed
	 */
	public void dismiss() {
		if(animationThread!=null && !animationThread.isInterrupted())
		{
			animationThread.interrupt();
			//animationThread.stop();
		}
		stopped = false;

		setVisibility(View.GONE);
	}
	
	public void clean()
	{
		dismiss();
		
		if(handler!=null)
		{
			handler.removeCallbacksAndMessages(null);
			handler=null;
		}
		animationThread=null;
		
		if(imageHolders!=null)
		{
			for(ImageView imview:imageHolders)
			{
				if(imview!=null)
				{
					if(imview.getBackground()!=null)
						imview.getBackground().setCallback(null);
					imview=null;
				}
			}
			imageHolders=null;
		}
		
		images=null;
	}

	/**
	 * Loads the layout and sets the initial set of images
	 */
	private void prepareLayout(boolean fillWidth,int numViews) 
	{
		//will be set to visibile when the animation starts
		setVisibility(View.INVISIBLE);
		
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.atprogressbar01, null);
		addView(view);

		imageHolders = new ArrayList<ImageView>();
		//if(numViews<3)
		//	numViews=3;

		if(fillWidth)
		{
			setLayoutParams
			(new MarginLayoutParams
					(ViewGroup.LayoutParams.MATCH_PARENT,20 ));
		}
		
		TableLayout.LayoutParams lParam=new TableLayout.LayoutParams(0,LayoutParams.MATCH_PARENT, 10f);
		if(numViews<=3)
		{
			imageHolders.add((ImageView) view.findViewById(R.id.imgOne));
			imageHolders.add((ImageView) view.findViewById(R.id.imgTwo));
			imageHolders.add((ImageView) view.findViewById(R.id.imgThree));
			
			ImageView imV=new ImageView(getContext());
			//android.view.ViewGroup.LayoutParams lParam= imageHolders.get(0).getLayoutParams();
			
			imV.setLayoutParams(lParam);
			imV.setTag(imageHolders.size()+1);
			//imV.setImageDrawable(new ColorDrawable(Color.YELLOW));
			imageHolders.add(imV);
			
			ImageView imV2=new ImageView(getContext());
			imV2.setLayoutParams(lParam);
			imV2.setTag(imageHolders.size()+1);
			//imV2.setImageDrawable(new ColorDrawable(Color.YELLOW));

			imageHolders.add(imV2);
			
			ImageView imV3=new ImageView(getContext());
			imV3.setLayoutParams(lParam);
			imV3.setTag(imageHolders.size()+1);
			//imV3.setImageDrawable(new ColorDrawable(Color.YELLOW));
			imageHolders.add(imV3);
			
			this.addView(imV);
			this.addView(imV2);
			this.addView(imV3);

		}
		else if((numViews>3))
		{
			ImageView imV=new ImageView(getContext());
			//android.view.ViewGroup.LayoutParams lParam= imageHolders.get(0).getLayoutParams();
			
			imV.setLayoutParams(lParam);
			imV.setTag(imageHolders.size()+1);
			imV.setImageDrawable(new ColorDrawable(Color.YELLOW));
			this.addView(imV);
			imageHolders.add(imV);
		}
		
		try{
			if(fillWidth)
			{
				
				
				//for(int i=0;i<imageHolders.size();i++)
				{
					//ScreenDimension.setMetrics(getContext());
					//int screenW=ScreenDimension.getScreenWidthPX();

					//if(getContext().getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE)	
					//	screenW=ScreenDimension.getScreenHeightPX();					

					//android.view.ViewGroup.LayoutParams lParam= imageHolders.get(i).getLayoutParams();
					//lParam.width=(screenW/imageHolders.size());
					//imageHolders.get(i).setLayoutParams(lParam);
					
					//imageHolders.get(i).setLayoutParams(new TableLayout.LayoutParams(0,getLayoutParams().height, 10f));

				}
				
				
			}
		}catch(Exception ex1){
		}

		
		
		// Prepare an array list of images to be animated
		images = new ArrayList<String>();

		images.add("progress_1");
		images.add("progress_2");
		images.add("progress_3");
		images.add("progress_4");
		images.add("progress_5");
		images.add("progress_6");
		images.add("progress_7");
		images.add("progress_8");
		images.add("progress_9");
	}

	/**
	 * Starts the animation thread
	 */
	public void startAnimation() 
	{
		setVisibility(View.VISIBLE);
		stopped=true;
		if(animationThread==null)
			animationThread = new Thread(this, "Progress");
		animationThread.start();
	}
	
	@Override
	public void run() {
		while (stopped) {
			try {
				if(!Thread.currentThread().isInterrupted())
				{// Sleep for 0.3 secs and after that change the images
				Thread.sleep(222);
				if(handler!=null)
					handler.sendEmptyMessage(0);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private AnimationHandler handler=new AnimationHandler(this);

	
	static class AnimationHandler extends Handler
	{
		private final WeakReference<ATProgressbar01> mTarget;
		
		AnimationHandler(ATProgressbar01 pbar)
		{
			mTarget=new WeakReference<ATProgressbar01>(pbar);
		}
		
		@Override
		public void handleMessage(Message msg) 
		{
			ATProgressbar01 pbar=mTarget.get();
			if(pbar==null)
				return;

			int currentImage = 0;
			int nextImage = 0;
			// Logic to change the images
			for (ImageView imageView : pbar.imageHolders) 
			{
				currentImage = Integer.parseInt(imageView.getTag().toString());
				if(_colors==null)
				{
					if (currentImage < 9) 
					{
						nextImage = currentImage + 1;
					} 
					else 
					{
						nextImage = 1;
					}
				}
				else
				{
					if (currentImage < _colors.length) 
					{
						nextImage = currentImage + 1;
					} 
					else 
					{
						nextImage = 1;
					}
				}
				imageView.setTag("" + nextImage);

				//imageView.setImageResource(ATUtil.getDrawableIdFromName(getContext(), images.get(nextImage - 1)));
				//imageView.setImageResource(getResourceIdFromName(getContext(),"drawable", images.get(nextImage - 1)));
				if(_colors==null)
				{
					imageView.setImageResource(pbar.getContext().getResources().getIdentifier(
                            pbar.images.get(nextImage - 1)
                            ,"color",
                            pbar.getContext().getPackageName()));
                            //ATUtil.getResourceIdFromName(pbar.getContext(), "color", pbar.images.get(nextImage - 1)));
				}
				else
				{
					imageView.setBackgroundColor(_colors[nextImage-1]);
				}

				//imageView.setImageDrawable(new ColorDrawable(Color.RED));
				//imageView.setImageResource(getResources().getIdentifier(
				//		images.get(nextImage - 1), "drawable",
				//		"com.antoniotari.android.util"));
			}
			super.handleMessage(msg);
		}
	}
}