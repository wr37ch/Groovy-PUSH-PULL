def action= System.getProperty('action')
def choice = System.getProperty('choice')
def version = System.getProperty('version')
def ArtifID_U = System.getProperty('Artname')
def GroupId_U = System.getProperty('Groupid')
def Version_U = System.getProperty('Vid')
def FilePath_U = System.getProperty('FP')

 //   while (true){
        def text
       // println 'What would you like to do? Write PULL/PUSH or exit'
       // def action = new Scanner(System.in).nextLine()
        if (action == "PULL") {
            println 'Type the name of the repo'
           // def choice = new Scanner(System.in).nextLine()

            def get = new URL($/http://50.50.50.50:8081/service/siesta/rest/beta/components?repositoryId=maventask-release/$).openConnection()
            def getRC = get.getResponseCode()
            println(getRC)
            if (getRC.equals(200)) {
                text = get.getInputStream().getText()

            }
            else if (!getRC.equals(200)){
                println("Can't connect to this repo, check repo name")
               
            }
            def slurper = new groovy.json.JsonSlurper()
            def result = slurper.parseText(text)

            for (int i = 0; i < result.items.size(); i++) {

                println("GAV parameters: ")
                print "(Name/ArtifactID — "+result.items[i].name + " | "
                print "Group — " +result.items[i].group + " | "
                print "Version — " +result.items[i].version + " | "
                println "id — "+result.items[i].id+ " )"

            }
          //  println "Write down which version of artifact you'd like to download"
           // def artifact = new Scanner(System.in).nextLine()
            for (int i = 0; i < result.items.size(); i++) {
                if (result.items[i].version == version ){
                    def a = result.items[i].assets[0].downloadUrl
                    new File("/opt/trash/Super-App-${version}"+".zip").withOutputStream { out ->
                        new URL(a).eachByte { b ->
                            out.write(b)
                        }
                    }
                    println("Succesfully downloaded")
                }

            }


        }
        else {

         //   println("Enter GroupID")
         //   def GroupId= new Scanner(System.in).nextLine()
          //  println("Enter ArtifID")
           // def ArtifID= new Scanner(System.in).nextLine()
           // println("Enter Version")
           // def Version= new Scanner(System.in).nextLine()


            URL url = new URL("http://50.50.50.50:8081/repository/maventask-release/$GroupId/$ArtifID/$Version/$ArtifID-$Version"+".zip")
            def authString = "admin:admin123".getBytes().encodeBase64().toString()
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("PUT");
            httpCon.setRequestProperty( "Authorization", "Basic ${authString}" )
            def file = new File("/opt/trash/Super-App-10.zip").bytes
            def out = new DataOutputStream(httpCon.outputStream)
            out.write(file);
            out.flush()
            out.close();
            println(httpCon.getResponseCode())




        }
        
        
    
