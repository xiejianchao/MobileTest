
package com.huhuo.mobiletest.view;

import java.util.ArrayList;
import java.util.List;

import org.xclcharts.chart.DialChart;
import org.xclcharts.common.MathHelper;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.plot.PlotAttrInfo;
import org.xclcharts.renderer.plot.Pointer;
import org.xclcharts.view.GraphicalView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;

/**
 * @ClassName DialChart例子
 * @Description  仪表盘例子
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */
public class DialChart03View extends GraphicalView {

	private String TAG = "DialChart03View";	
	
	private DialChart chart = new DialChart();
	private float mPercentage = 0.0f;
	
	public DialChart03View(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();
	}
	
	public DialChart03View(Context context, AttributeSet attrs){   
        super(context, attrs);   
        initView();
	 }
	 
	 public DialChart03View(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			initView();
	 }
	 
	 
	 private void initView()
	 {
		chartRender();
	 }
	 
	 @Override  
	    protected void onSizeChanged(int w, int h, int oldw, int oldh) {  
	        super.onSizeChanged(w, h, oldw, oldh);  
	        chart.setChartRange(w ,h ); 
	    }  		
						
		public void chartRender()
		{
			try {								
							
				//设置标题背景			
				chart.setApplyBackgroundColor(true);
//				chart.setBackgroundColor( Color.rgb(28, 129, 243) );
				chart.setBackgroundColor( Color.TRANSPARENT);//不绘制背景
				//绘制边框
//				chart.showRoundBorder();
						
				//设置当前百分比
				chart.getPointer().setPercentage(mPercentage);
				
				//设置指针长度
				chart.getPointer().setLength(0.75f);
				
				//增加轴
				addAxis();						
				/////////////////////////////////////////////////////////////
				//增加指针
//				addPointer();
				//设置附加信息
				addAttrInfo();
				/////////////////////////////////////////////////////////////
				
				
				chart.getPointer().setPercentage(mPercentage);
				
				chart.getPointer().getPointerPaint().setColor(Color.WHITE);
				chart.getPointer().getBaseCirclePaint().setColor(Color.WHITE);
				//chart.getPointer().setPointerStyle(XEnum.PointerStyle.TRIANGLE);
				
				chart.getPointer().setPercentage(mPercentage/2);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
			}
			
		}
		
		public void addAxis()
		{
			
			List<Float> ringPercentage = new ArrayList<Float>();			
			float rper = MathHelper.getInstance().div(1, 4); //相当于40%	//270, 4
			ringPercentage.add(rper);
			ringPercentage.add(rper);
			ringPercentage.add(rper);
			ringPercentage.add(rper);
			
			List<Integer> rcolor  = new ArrayList<Integer>();			
			rcolor.add(Color.rgb(242, 110, 131));
			rcolor.add(Color.rgb(238, 204, 71));
			rcolor.add(Color.rgb(42, 231, 250));
			rcolor.add(Color.rgb(140, 196, 27));						
			chart.addStrokeRingAxis(0.95f,0.8f, ringPercentage, rcolor);
			
			//增加轴的数量
			List<String> rlabels  = new ArrayList<String>();
			rlabels.add("0M");
			rlabels.add("10M");
			rlabels.add("20M");
			rlabels.add("30M");
			rlabels.add("40M");	
			rlabels.add("50M");
			rlabels.add("60M");
			rlabels.add("70M");
			rlabels.add("80M");
			rlabels.add("90M");
			rlabels.add("100M");
			chart.addInnerTicksAxis(0.8f, rlabels);
			
						
			chart.getPlotAxis().get(0).getFillAxisPaint().setColor(Color.rgb(28, 129, 243));
			chart.getPlotAxis().get(1).getFillAxisPaint().setColor(Color.rgb(28, 129, 243));
			chart.getPlotAxis().get(1).getTickLabelPaint().setColor(Color.GREEN);
			chart.getPlotAxis().get(1).getTickMarksPaint().setColor(Color.WHITE);
			chart.getPlotAxis().get(1).hideAxisLine();
			chart.getPlotAxis().get(1).setDetailModeSteps(3);
			
//			chart.getPointer().setPointerStyle(XEnum.PointerStyle.TRIANGLE);
//			chart.getPointer().getPointerPaint().setColor(Color.rgb(217, 34, 34) );
//			chart.getPointer().getPointerPaint().setStrokeWidth(3);			
//			chart.getPointer().getPointerPaint().setStyle(Style.STROKE);			
//			chart.getPointer().hideBaseCircle();
			
		}
		
		//增加指针
//		public void addPointer()
//		{					
//			chart.addPointer();			
//			List<Pointer> mp = chart.getPlotPointer();	
//			mp.get(0).setPercentage( mPercentage);
//			//设置指针长度
//			mp.get(0).setLength(0.75f);	
//			mp.get(0).getPointerPaint().setColor(Color.RED);
//			mp.get(0).setPointerStyle(XEnum.PointerStyle.TRIANGLE);			
//			mp.get(0).hideBaseCircle();
//			
//		}
		
		
		private void addAttrInfo()
		{
			/////////////////////////////////////////////////////////////
			PlotAttrInfo plotAttrInfo = chart.getPlotAttrInfo();
			//设置附加信息
			Paint paintTB = new Paint();
			paintTB.setColor(Color.WHITE);
			paintTB.setTextAlign(Align.CENTER);
			paintTB.setTextSize(30);	
			paintTB.setAntiAlias(true);	
			plotAttrInfo.addAttributeInfo(XEnum.Location.TOP, "当前网速", 0.3f, paintTB);
			
			Paint paintBT = new Paint();
			paintBT.setColor(Color.WHITE);
			paintBT.setTextAlign(Align.CENTER);
			paintBT.setTextSize(35);
			paintBT.setFakeBoldText(true);
			paintBT.setAntiAlias(true);	
			plotAttrInfo.addAttributeInfo(XEnum.Location.BOTTOM, 
					Float.toString(MathHelper.getInstance().round(mPercentage * 100,2)), 0.3f, paintBT);
			
			Paint paintBT2 = new Paint();
			paintBT2.setColor(Color.WHITE);
			paintBT2.setTextAlign(Align.CENTER);
			paintBT2.setTextSize(30);
			paintBT2.setFakeBoldText(true);
			paintBT2.setAntiAlias(true);	
			plotAttrInfo.addAttributeInfo(XEnum.Location.BOTTOM, "MB/S", 0.4f, paintBT2);				
		}
		
		public void setCurrentStatus(float percentage)
		{								
			mPercentage =  percentage;
			chart.clearAll();
			
			//设置当前百分比
			chart.getPointer().setPercentage(mPercentage);
			addAxis();
			//增加指针
//			addPointer();
			addAttrInfo();
			invalidate();
		}


		@Override
		public void render(Canvas canvas) {
			// TODO Auto-generated method stub
			 try{
		            chart.render(canvas);
		        } catch (Exception e){
		        	Log.e(TAG, e.toString());
		        }
		}

}
