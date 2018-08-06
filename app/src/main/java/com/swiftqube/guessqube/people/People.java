package com.swiftqube.guessqube.people;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class People {
	private static boolean playBoth;
	private static int Total;
	public static String Table, Banner, DTH="";
	public static int index;
	public static int rand;
	public static int pix; public static String[] bx = new String[5], lbx = new String[4], rbx = new String[4];
	public static String[] tbx = new String[3], mbx = new String[3], bbx = new String[3];

	//ArrayList for each category to store
	public static ArrayList<Integer> holder = new ArrayList<Integer>();
	public static ArrayList<Integer> holder1 = new ArrayList<Integer>();
	public static ArrayList<Integer> holder2 = new ArrayList<Integer>();
	public static ArrayList<Integer> holder4 = new ArrayList<Integer>();
	public static ArrayList<Integer> holder5 = new ArrayList<Integer>();
	public static ArrayList<Integer> holder6 = new ArrayList<Integer>();
	public static ArrayList<Integer> list;
	public static ArrayList<Integer> guyslist = new ArrayList<Integer>();
	public static ArrayList<Integer> ladieslist = new ArrayList<Integer>();
	Random random = new Random();
	private static int guysTotal, ladiesTotal;

	public static int[] Hscore = new int[7];
	public static int[] Saved = new int[7], life = new int[7], timer = new int[7];
	public static int pix_1, pix_2, pix_3;
	
	public People(){
		list = new ArrayList<Integer>(Total);
	}
	
	public int getIndex(){
		return index;
	}
	
	public int getLife(){
		return life[index];
	} 
	
	public int getSaved(){
		return Saved[index];
	}
	
	public void setTable(String table, int index, String banner){
		Table = table;
		this.index = index;
		this.Banner = banner;
		playBoth = false;
	}

	public int Randomize(int caller){
		getHolder();
		Log.i("Holder", "holder is "+ holder.toString());
		if(list.isEmpty() || caller==1){
		    //creates list of the pictures. Equal to the Total number of pictures in that category
			for(int i = 1; i <= Total; i++)
				list.add(i); //adds number to list

			//holder is a cache for already seen pictures.
            if(holder.size() > 0){
				for(int compare: holder)
				{
					if(list.contains(compare))
						list.remove(list.indexOf(compare));						
				}
			}
		}
		//this returns a random number from the list of pictures
		rand = random.nextInt(list.size());
		rand = list.remove(rand);
		holder.add(rand); //adds the number to seen images


		if(holder.size()>40)
			holder.remove(0); //ensures holder doesn't exceed 40 seen images
		
		setHolder();
		return rand;
	}
	
	public int RandomizeBoth(int mode){
		switch(mode){
		case 1:
			holder = holder1;
			list = guyslist;
			Total = guysTotal; break;
		case 2:
			holder = holder2;
			list = ladieslist;
			Total = ladiesTotal; break;
		}
		if(list.isEmpty()){
			for(int i = 1; i <= Total; i++)
				list.add(i);
			
			if(holder.size() > 0){
				for(int compare: holder)
				{
					if(list.contains(compare))
						list.remove(list.indexOf(compare));						
				}				
			}
		}		
		rand = random.nextInt(list.size());
		rand = list.remove(rand);
		holder.add(rand);
		
		if(holder.size()>40)
			holder.remove(0);
		
		switch(mode){
		case 1:
			holder1 = holder;
			guyslist = list; break;
		case 2:
			holder2 = holder;
			ladieslist = list; break;
		}
		return rand;
	}
	
	public void setTotal(int total){
		Total = total;
	}
	
	public int getHScore(){
		return Hscore[index];
	}
	
	public String getPrefix(){
		char c = Table.charAt(0);
		return String.valueOf(c);
	}

	public void saveAll(int hscore, int llife, int saved){
		Hscore[index] = hscore;
		life[index] = llife;
		Saved[index] =saved;
	}
	
	public void getHolder(){
		switch(index){
		case 1:
			holder = holder1; break;
		case 2:
			holder = holder2; break;
		case 4:
			holder = holder4; break;
		case 5:
			holder = holder5; break;
		case 6:
			holder = holder6; break;
		}
	}
	
	public void setHolder(){
		switch(index){
		case 1:
			holder1 = holder; break;
		case 2:
			holder2 = holder; break;
		case 4:
			holder4 = holder; break;
		case 5:
			holder5 = holder; break;
		case 6:
			holder6 = holder; break;
		}
	}
	
	public String getBanner(){
		return Banner;
	}

	public void bothEngine(int guysTotal, int ladiesTotal, String banner){
		playBoth = true;
		this.index = 3;
		this.Banner = banner;
		this.guysTotal = guysTotal;
		this.ladiesTotal = ladiesTotal;
		
		for(int i = 1; i <= guysTotal; i++)
			guyslist.add(i);
		for(int i = 1; i <= ladiesTotal; i++)
			ladieslist.add(i);
	}

	public boolean getMode(){
		return playBoth;
	}

	public void setState(int hScore, int life, int saved){
		this.Hscore[index] = hScore;
		this.life[index] = life;
		this.Saved[index] = saved;
	}

	public String getHVar(){
		String hvar = "";
		switch (getIndex()){
			case 1:
				hvar = "HGUYS";
				DTH = "DTH1"; break;
			case 2:
				hvar = "HLADIES";
				DTH = "DTH2"; break;
			case 3:
				hvar = "HBOTH";
				DTH = "DTH3"; break;
			case 4:
				hvar = "HCOMICS";
				DTH = "DTH4";  break;
			case 5:
				hvar = "HPLACES";
				DTH = "DTH5"; break;
			case 6:
				hvar = "HFAMOUS";
				DTH = "DTH6";  break;
		}
		return hvar;
	}

	public String getDTH(){
		return DTH;
	}
	
	public void savedGame(int pix, String b1, String b2, String b3, String b4){
		this.pix = pix;
		bx[1] = b1; bx[2] = b2; bx[3] = b3; bx[4] = b4; 
	}
	
	public void savedGame(int pix_1, int pix_2, String lb1, String lb2, String lb3, String rb1, String rb2, String rb3){
		this.pix_1 = pix_1; this.pix_2 = pix_2;
		lbx[1] = lb1; lbx[2] = lb2; lbx[3] = lb3; rbx[1] = rb1; rbx[2] = rb2; rbx[3] = rb3;
	}
	
	public void savedGame(int pix_1, int pix_2, int pix_3, String tb1, String tb2, String mb1, String mb2, String bb1, String bb2){
		this.pix_1 = pix_1; this.pix_2 = pix_2; this.pix_3 = pix_3;
		tbx[1] = tb1; tbx[2] = tb2; mbx[1] = mb1; mbx[2] = mb2; bbx[1] = bb1; bbx[2] = bb2;
	}

	public String endGame(int score, int date, int time){
		String[] alpha = {"A", "B", "C", "D", "E", "K", "L", "M", "N", "O", "U", "V", "W", "X", "Y", "Z"};
		date = (10+date) % 7;
		time = time % 6;
		score = (10 * (1 + random.nextInt(9))) + (score % 8);
		
		String[] a = new String[5];
		for(int i = 0; i <= 4; i++){
			a[i] = alpha[random.nextInt(16)];
		}		
		
		String hash = a[0] + a[1] + date + a[2] + a[3] + score + a[4] + time;
		return hash;
	}
}