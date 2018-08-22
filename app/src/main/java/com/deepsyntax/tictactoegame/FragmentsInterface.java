package com.deepsyntax.tictactoegame;

import java.util.*;

public interface FragmentsInterface
{
	public void OnPlayerSettingsComplete(ArrayList<String> playerNames);
	public void OnGameSettingsComplete(ArrayList<String>playerNames,int themeIndex,int playerSymbol,int numBoardGrid,int rounds);
	public void OnUploadImage(int playerSettingsIndex);
}
