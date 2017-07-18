Map configOptions;
char[] configText;
// volatile 
boolean initialized = false;
 
// In Thread A
configOptions = new HashMap();
configText = readConfigFile(fileName);
processConfigOptions(configText, configOptions); // continue setting configOptions
initialized = true;		// all preceding operation should be done by this operation

// In Thread B
while (!initialized) 
  sleep();
// use configOptions