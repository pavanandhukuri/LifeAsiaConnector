package org.dh.p400.translator;

public class XmlToMainframePadding
{
    /**
     * This method generates string message in COBOL format.
     * @param copybook name
     * @param PICTURE size
     * @param the value of field
     * @param BodyBlock
     * @return string
     */
	public static void main(String[]args)throws Exception
	{
		System.out.println(XmlToMainframePadding.transformXmlToMainframe("asdasd", "9(02)V9(01)", "1"));
		System.out.println(normalizeSize("99V9"));
	}
    public static String transformXmlToMainframe ( String copybookFieldName,
        String size, String value ) throws InvalidValueException
    {
        size = normalizeSize(size);
        boolean flag = false;
        int indexOfV = size.indexOf ( 'V' );
        String sizeBeforeV = null;
        String sizeAfterV = null;
       

        if ( indexOfV != -1 )
        {
            flag = true;
            sizeBeforeV = size.substring ( 0, indexOfV );
            sizeAfterV = size.substring ( indexOfV + 1, size.length (  ) );
          
        }
        
        

        StringBuffer messageBuffer = new StringBuffer(  );
        char firstChar = size.charAt ( 0 );

        if ( ( firstChar == 'X' ) || ( firstChar == 'A' ) )
        {
            /**
             * This method generates string message in COBOL format for Alphanumeric and Alphabetics.
             * @param copybook name
             * @param PICTURE size
             * @param the value of field
             * @param message as StringBuffer
             * @param BodyBlock
             * @return string
             */
            setMessageForX ( copybookFieldName, size, value, messageBuffer );
        }
        else if ( ( firstChar == 'S' ) && ( flag == true ) )
        {
            /**
             * This method generates string message in COBOL format for real values which is having
             * PICTURE size with SignTrailingSeperate or SignLeadingSeperate,ex:S9(005)V9(002).
             * @param copybook name
             * @param PICTURE size
             * @param the value of field
             * @param PICTURE size as String which comes before V
             * @param PICTURE size as String which comes after V
             * @param message as StringBuffer
             * @param BodyBlock
             * @return string
             */
            setMessageWhenV ( copybookFieldName, size, sizeBeforeV, sizeAfterV,
                value, messageBuffer );
        }
        else if ( ( firstChar == 'S' ) && ( flag == false ) )
        {
            /**
             * This method generates string message in COBOL format for integer values,ex:S9(005).
             * @param copybook name
             * @param PICTURE size
             * @param the value of field
             * @param PICTURE size as String which comes before V
             * @param PICTURE size as String which comes after V
             * @param message as StringBuffer
             * @param BodyBlock
             * @return string
              */
            setMessageWhenNoV ( copybookFieldName, size, value, messageBuffer );
        }
        else if ( ( firstChar == '9' ) && ( flag == true ) )
        {
            /**
             * This method generates string message in COBOL format for real values,
             * PICTURE size without SignTrailingSeperate or SignLeadingSeperate,ex:9(005)V9(002).
             * @param copybook name
             * @param PICTURE size
             * @param the value of field
             * @param PICTURE size as String which comes before V
             * @param PICTURE size as String which comes after V
             * @param message as StringBuffer
             * @param BodyBlock
             * @return string
              */
            setMessageWhenV ( copybookFieldName, size, sizeBeforeV, sizeAfterV,
                value, messageBuffer );
        }
        else if ( ( firstChar == '9' ) && ( flag == false ) )
        {
            /**
             * This method generates string message in COBOL format for integer values,ex:9(005).
             * @param copybook name
             * @param PICTURE size
             * @param the value of field
             * @param PICTURE size as String which comes before V
             * @param PICTURE size as String which comes after V
             * @param message as StringBuffer
             * @param BodyBlock
             * @return string
              */
            setMessageWhenNoV ( copybookFieldName, size, value, messageBuffer );
        }
        else if ( firstChar == 'd' )
        {
            /**
             * This method generates string message in COBOL format for integer values,ex:9(005).
             * @param copybook name
             * @param PICTURE size
             * @param the value of field
             * @param PICTURE size as String which comes before V
             * @param PICTURE size as String which comes after V
             * @param message as StringBuffer
             * @param BodyBlock
             * @return string
             */
            setMessageForDate ( copybookFieldName, size, value, messageBuffer );
        }

        return messageBuffer.toString (  );
    }
	private static String normalizeSize(String size) {
		//char[] charArray = size.toCharArray();
    	/*
    	 * Start of Modification due to different forms of picture like 999V99 can be written as
    	 * 9(03)V9(02), similarly, XXX can be writen as X(03)
    	 * by Arijit
    	 */
    	
    	boolean normalizeBefore=false;
    	boolean normalizeAfter=false;
    	int indexV=size.indexOf("V");
    	if(indexV!=-1)
    	{
    		
    		String sizeBeforeV = size.substring ( 0, indexV );
            String sizeAfterV = size.substring ( indexV + 1, size.length (  ) );
            if(sizeBeforeV.indexOf("(")==-1)
            {
            	normalizeBefore=true;
            }else if(sizeAfterV.indexOf("(")==-1)
            {
            	normalizeAfter=true;
            }
    	}
    	

    	
    	if(size.indexOf("(")==-1 || (normalizeBefore ||normalizeAfter))
    	{
    		char firstchar=size.charAt(0);
    		int indexOfV= size.indexOf ('V');
    		if(firstchar=='X')
    		{
    			size="X("+size.length()+")";
    			 
    		}else if(firstchar=='9' &&  indexOfV !=-1)
    		{
    			String sizeBeforeV = size.substring ( 0, indexOfV );
                String sizeAfterV = size.substring ( indexOfV + 1, size.length (  ) );
                
                
                if(normalizeBefore)
                {
                	
                	size="9("+sizeBeforeV.length()+")V"+sizeAfterV;	
                }else if(normalizeAfter)
                {
                 	size=sizeBeforeV+"V"+"9("+sizeAfterV.length()+")";	
                	
                }else
                {
                size="9("+sizeBeforeV.length()+")V"+"9("+sizeAfterV.length()+")";
                }
                
    		}else if(firstchar=='S' &&  indexOfV !=-1)
    		{
    			String sizeBeforeV = size.substring ( 0, indexOfV );
                String sizeAfterV = size.substring ( indexOfV + 1, size.length (  ) );
                if(normalizeBefore)
                {
                	size="S9("+(sizeBeforeV.length()-1)+")V"+"9("+sizeAfterV;
                }else if(normalizeAfter)
                {
                	size=sizeBeforeV+"V"+"9("+sizeAfterV.length()+")";
                }else
                {
                	size="S9("+(sizeBeforeV.length()-1)+")V"+"9("+sizeAfterV.length()+")";
                }
                
    		}else if(firstchar=='9' &&  indexOfV ==-1)
    		{
    			size="9("+size.length()+")";
    			 
    		}else if(firstchar=='S' &&  indexOfV ==-1)
    		{
    			size="S9("+(size.length()-1)+")";
    		}
    	}
    	
    
		return size;
	}

    private static void setMessageForX ( String copybookFieldName, String size,
        String value, StringBuffer messageBuffer ) throws InvalidValueException
    {
        char blank = ' ';
        Integer fieldLengthObject = Integer.valueOf ( size.substring ( 2,
                    size.length (  ) - 1 ) );
        int fieldLength = fieldLengthObject.intValue (  );
     
        if ( value.length (  ) < fieldLength )
        {
            int _blanks = fieldLength - value.length (  );
            messageBuffer.append ( value );

            for ( int i = 0; i < _blanks; i++ )
            {
                messageBuffer.append ( blank );
            }
        }
        else if ( value.length (  ) == fieldLength )
        {
            messageBuffer.append ( value );
        }
        else if ( value.length (  ) > fieldLength )
        {
            System.out.println ( 
                "The length of the value must not be greater than size" );
            messageBuffer.append ( "invalid_value" );
            throw new InvalidValueException("Invalid Value", copybookFieldName);
        }
    }

    private static void setMessageWhenV ( String copybookFieldName,
        String size, String sizeBeforeV, String sizeAfterV, String value,
        StringBuffer messageBuffer ) throws InvalidValueException
    {
    	
    	boolean signed = false; // Senthil
        String sign = "+"; // Senthil
        String s="";
        try
        {
            Integer fieldLengthObjectBeforeV = null;

            if ( ( sizeBeforeV.charAt ( 0 ) ) == 'S' )
            {
                fieldLengthObjectBeforeV = Integer.valueOf ( sizeBeforeV.substring ( 
                            3, sizeBeforeV.length (  ) - 1 ) );
                
                signed = true; // Senthil
                sign = "+"; // Senthil
                s=" ";
            }
            else if ( ( sizeBeforeV.charAt ( 0 ) ) == '9' )
            {
                fieldLengthObjectBeforeV = Integer.valueOf ( sizeBeforeV.substring ( 
                            2, sizeBeforeV.length (  ) - 1 ) );
                
                signed = false; // Senthil
                sign = ""; // Senthil
                s="";
            }
            else
            {
                System.out.println ( "program doesn't handle other than S, 9" );
            }

            int fieldLengthBeforeV = fieldLengthObjectBeforeV.intValue (  );
            Integer fieldLengthObjectAfterV = Integer.valueOf ( sizeAfterV.substring ( 
                       2, sizeAfterV.length (  ) - 1 ) );
            

            int fieldLengthAfterV = fieldLengthObjectAfterV.intValue (  );
            
            if(value.equals("*"))
        	{
        		messageBuffer.append(value);
        		int length=fieldLengthBeforeV+fieldLengthAfterV-value.length();
        		for(int i=0;i<length;i++)
        		{
        			messageBuffer.append(' ');
        		}
        		messageBuffer.append(s);
        	}else if(value.equals("S"))
        	{
        		int length=fieldLengthBeforeV+fieldLengthAfterV;
        		for(int i=0;i<length;i++)
        		{
        			messageBuffer.append(' ');
        		}
        		messageBuffer.append(s);	
        	}else
        	{

            int indexOfDot = value.indexOf ( '.' );

            if ( indexOfDot != -1 )
            {
                String valueBeforeDot = value.substring ( 0, indexOfDot );
            
                if (signed)
                {
	                if (valueBeforeDot.charAt(0) == '-')
	                {
	                	sign = "-";
	                	valueBeforeDot = valueBeforeDot.substring(1);
	                } else if (valueBeforeDot.charAt(0) == '+')
	                {
	                	sign = "+";
	                	valueBeforeDot = valueBeforeDot.substring(1);
	                }
                }
                else
                {
                	sign = "";
                }
             

                String valueAfterDot = value.substring ( indexOfDot + 1,
                        value.length (  ) );

                int valueLengthBeforeDot = valueBeforeDot.length (  );

                int valueLengthAfterDot = valueAfterDot.length (  );

                if ( valueLengthAfterDot > fieldLengthAfterV )
                {
                    System.out.println ( 
                        "Value**** after decimal should not be more" );
                    messageBuffer.append ( "invalid_value" );
                    throw new InvalidValueException("Invalid Value", copybookFieldName);
                }

                if ( valueLengthBeforeDot < fieldLengthBeforeV )
                {
                    int zeros = fieldLengthBeforeV - valueLengthBeforeDot;

                    for ( int zero = 0; zero < zeros; zero++ )
                        messageBuffer.append ( '0' );

                    messageBuffer.append ( valueBeforeDot );
                }
                else if ( valueLengthBeforeDot == fieldLengthBeforeV )
                {
                    messageBuffer.append ( valueBeforeDot );
                }
                else if ( valueLengthBeforeDot > fieldLengthBeforeV )
                {
                    System.out.println ( 
                        "The give value has more length before Dot" );
                    messageBuffer.append ( "invalid_value" );
                    throw new InvalidValueException("Invalid Value", copybookFieldName);
                }

                //messageBuffer.append('.');
                if ( valueLengthAfterDot < fieldLengthAfterV )
                {
                    messageBuffer.append ( valueAfterDot );

                    for ( int decimalZero = 0;
                            decimalZero < ( fieldLengthAfterV -
                            valueLengthAfterDot ); decimalZero++ )
                        messageBuffer.append ( '0' );
                    
                    messageBuffer.append(sign); // Senthil
                }
                else if ( valueLengthAfterDot == fieldLengthAfterV )
                {
                    messageBuffer.append ( valueAfterDot );
                    messageBuffer.append(sign); // Senthil
                }
                else if ( valueLengthAfterDot > fieldLengthAfterV )
                {
                    System.out.println ( 
                        "Value after decimal should not be more" );
                    messageBuffer.append ( "invalid_value" );
                    throw new InvalidValueException("Invalid Value", copybookFieldName);
                }
            }
            else
            {
                
                if (signed)
                {
	                if (value.charAt(0) == '-')
	                {
	                	sign = "-";
	                	value = value.substring(1);
	                } else if (value.charAt(0) == '+')
	                {
	                	sign = "+";
	                	value = value.substring(1);
	                }
                }
                else
                {
                	sign = "";
                }
           
                char [] valueArray = value.toCharArray (  );
                int lengthWhenV = fieldLengthBeforeV + fieldLengthAfterV;
                if ( value.length (  ) > lengthWhenV )
                {
                    System.out.println ( "value length is more without dot" );
                    messageBuffer.append ( "invalid_value" );
                    throw new InvalidValueException("Invalid Value", copybookFieldName);
                }
                else if ( value.length (  ) == lengthWhenV )
                {
                    int h = 0;

                    for ( int f = 0; f < fieldLengthBeforeV; f++ )
                    {
                        messageBuffer.append ( valueArray[f] );
                        h++;
                    }

                    //messageBuffer.append('.');
                    for ( int g = h; g < valueArray.length; g++ )
                        messageBuffer.append ( valueArray[g] );
                    
                    messageBuffer.append(sign); // Senthil
                }
                else if ( value.length (  ) < lengthWhenV )
                {
                    if ( value.length (  ) < fieldLengthBeforeV )
                    {
                        for ( int addZero = 0;
                                addZero < ( fieldLengthBeforeV -
                                value.length (  ) ); addZero++ )
                            messageBuffer.append ( '0' );

                        messageBuffer.append ( value );

                        //messageBuffer.append('.');
                        for ( int zeroAfterDecimal = 0;
                                zeroAfterDecimal < fieldLengthAfterV;
                                zeroAfterDecimal++ )
                            messageBuffer.append ( '0' );
                        
                        messageBuffer.append(sign); // Senthil
                    }
                    else if ( value.length (  ) > fieldLengthBeforeV )
                    {
                        int b = 0;

                        for ( int a = 0; a < fieldLengthBeforeV; a++ )
                        {
                            messageBuffer.append ( valueArray[a] );
                            b++;
                        }

                        //messageBuffer.append('.');
                        if ( ( valueArray.length - b ) == fieldLengthAfterV )
                        {
                            for ( int c = b; c < ( valueArray.length - b );
                                    c++ )
                            {
                                System.out.println ( "checking without dot " +
                                    valueArray[c] );
                                messageBuffer.append ( valueArray[c] );
                            }
                        }
                        else if ( ( valueArray.length - b ) < fieldLengthAfterV )
                        {
                            int m = b;
                            int k = 0;

                            for ( int c = 0; c < ( valueArray.length - b );
                                    c++ )
                            {
                                messageBuffer.append ( valueArray[m] );
                                k++;
                                m++;
                            }

                            for ( int l = 0; l < ( fieldLengthAfterV - k );
                                    l++ )
                                messageBuffer.append ( '0' );
                        }
                        messageBuffer.append(sign); // Senthil
                    }
                    else if ( value.length (  ) == fieldLengthBeforeV )
                    {
                        messageBuffer.append ( value );

                        //messageBuffer.append('.');
                        for ( int e = 0; e < fieldLengthAfterV; e++ )
                            messageBuffer.append ( '0' );
                        
                        messageBuffer.append(sign); // Senthil
                    }
                }
            }
        	}
        }
        catch ( NumberFormatException nfe )
        {
            System.out.println ( "Exception in setMessageWhenV  :" +
                nfe.getMessage (  ) );
            messageBuffer.append ( "invalid_value" );
            throw new InvalidValueException("Invalid Value", copybookFieldName);
        }
        
    }

    private static void setMessageWhenNoV ( String copybookFieldName,
        String size, String value, StringBuffer messageBuffer ) throws InvalidValueException
    {
        int indexOfDot = value.indexOf ( '.' );

        if ( indexOfDot != -1 )
        {
            setMessageFor9 ( copybookFieldName, size,
                value.substring ( 0, indexOfDot ), messageBuffer );
        }
        else
        {
            setMessageFor9 ( copybookFieldName, size, value, messageBuffer );
        }
    }

    private static void setMessageFor9 ( String copybookFieldName, String size,
        String value, StringBuffer messageBuffer ) throws InvalidValueException
    {
        int lengthForS = 0;
    	boolean signed = false;
        String sign = "+";
        String s="";

        if ( size.charAt ( 0 ) == 'S' )
        {
            lengthForS = ( Integer.valueOf ( size.substring ( 3,
                        size.length (  ) - 1 ) ) ).intValue ();
            signed = true;
            sign = "+"; 
            s=" ";
        }

        else if ( size.charAt ( 0 ) == '9' )
        {
            lengthForS = ( Integer.valueOf ( size.substring ( 2,
                        size.length (  ) - 1 ) ) ).intValue (  );
            signed = false; 
            sign = ""; 
            s="";
        }
        else
        {
            System.out.println ( 
                "Doesn't handle other than S and 9 when no V in it" );
            messageBuffer.append ( "invalid_value" );
            throw new InvalidValueException("Invalid Value", copybookFieldName);
        }
       
        if (signed)
        {
            if (value.charAt(0) == '-')
            {
            	sign = "-";
            	value = value.substring(1);
            } else if (value.charAt(0) == '+')
            {
            	sign = "+";
            	value = value.substring(1);
            }
        }
        else
        {
        	sign = "";
        }
        
        if ( value.length (  ) > lengthForS )
        {
            System.out.println ( "value length is more than size" );
            messageBuffer.append ( "invalid_value" );
            throw new InvalidValueException("Invalid Value", copybookFieldName);
        }
        else if ( value.length () < lengthForS )
        {
        	//Modified to accomodate BO handling 
        	/*
        	 * �*� after the Field Name indicates that the Value can be 
        	 * overridden with an asterisk (�*�) character. This tells the Business 
        	 * Object to accept the current or defaulted value. When formatting asterisk (�*�)
        	 *  in a numeric field, you should consider the field changed to alphanumeric left
        	 *  justified and space filled.
        	 */
        	 if(value.indexOf("*")!=-1)
        	 {
        		 messageBuffer.append ( value ); 
        		 for ( int p = 0; p < ( lengthForS - value.length () ); p++ )
        		 {
        			 messageBuffer.append(' ');
        		 }
        		 messageBuffer.append(s);
        	 }else if(value.equals("S"))
        	 {
        		 for ( int p = 0; p <lengthForS; p++ )
        		 {
        			 messageBuffer.append(' ');
        		 }
        		 messageBuffer.append(s);
        	 }else
        	 {
        		  for ( int p = 0; p < ( lengthForS - value.length (  ) ); p++ )
        		  {
        			  messageBuffer.append ( '0' );  
        		  }
        		  messageBuffer.append ( value );
                  messageBuffer.append(sign);
        	 }
          
                
          
        }
        else if ( value.length () == lengthForS )
        {
        	if(value.equals("S"))
        	{
        		 messageBuffer.append (" ");
        		 messageBuffer.append (s);
        	}else
        	{
            messageBuffer.append ( value );
            messageBuffer.append(sign);
        	}
        }
    }

    private static void setMessageForDate ( String copybookFieldName,
        String size, String value, StringBuffer messageBuffer )
    {
        if ( value.length (  ) != 0 )
        {
            messageBuffer.append ( value.substring ( 0, 4 ) );
            messageBuffer.append ( value.substring ( 5, 7 ) );
            messageBuffer.append ( value.substring ( 8, 10 ) );
        }
        else
        {
            messageBuffer.append ( "99999999" );
        }
    }

    public static String transformMainframeToXml ( String copybookFieldName,
        String size, String value )
    {
        return "do stuff for passing p400 responses to Cordys";
    }
}
