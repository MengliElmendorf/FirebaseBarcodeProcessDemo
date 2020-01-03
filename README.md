## FirebaseBarcodeProcessDemo can be use as a Library Project import into your other android Project
  #### 1. If your android project haven't configure Kotlin in project, you will need to configure Kotlin in your android project by doing following process:
     - Tools -> Kotlin -> Configure Kotlin in Project
  #### 2. Create .AAR file:
     - Build -> Rebuild Project
     - .AAR file will be created under this directory: app/build/outputs/aar 
  #### 3. Import .AAR file:
     - Go to your android project
     - Project -> New -> Module -> Import .JAR/.AAR Package
  #### 4. Configure dependencies:
     - Go to module gradle add following dependencies:
        implementation 'com.google.firebase:firebase-ml-vision:24.0.1'
        implementation 'com.google.firebase:firebase-ml-vision-barcode-model:16.0.2'
        implementation 'com.google.firebase:firebase-ml-vision-image-label-model:19.0.0'
        implementation project (":FirebaseBarcodeScanerLib")
     
    
