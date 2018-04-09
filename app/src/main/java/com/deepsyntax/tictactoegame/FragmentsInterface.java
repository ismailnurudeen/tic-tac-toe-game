package com.deepsyntax.tictactoegame;

import java.util.*;

public interface FragmentsInterface
{
	public void OnPlayerSettingsComplete(ArrayList<String> playerNames);
	public void OnGameSettingsComplete(ArrayList<String>playerNames,int rounds);
	//public void OnPlayerDataRecieved(String name,byte imageByte,String player);
}
