// code to generate X barcodes, store in a DB, then send them via bluetooth to a handheld printer 

public class MainActivity extends AppCompatActivity {
   private static final int REQUEST_ENABLE_BT = 1;
   private BluetoothAdapter mBluetoothAdapter;
   private BluetoothDevice mDevice;
   private BluetoothSocket mSocket;
   private OutputStream mOutputStream;
   private DatabaseHelper mDbHelper;
   private EditText mNumBarcodes;
   private Button mGenerateBarcodes;
   private Button mPrintBarcodes;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      mNumBarcodes = findViewById(R.id.num_barcodes);
      mGenerateBarcodes = findViewById(R.id.generate_barcodes);
      mPrintBarcodes = findViewById(R.id.print_barcodes);
      mDbHelper = new DatabaseHelper(this);

      mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
      if (mBluetoothAdapter == null) {
         Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
         finish();
      }

      mGenerateBarcodes.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            int numBarcodes = Integer.parseInt(mNumBarcodes.getText().toString());
            generateBarcodes(numBarcodes);
         }
      });

      mPrintBarcodes.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            printBarcodes();
         }
      });
   }

   private void generateBarcodes(int numBarcodes) {
      SQLiteDatabase db = mDbHelper.getWritableDatabase();
      ContentValues values = new ContentValues();
      for (int i = 0; i < numBarcodes; i++) {
         values.put(DatabaseContract.BarcodeEntry.COLUMN_NAME_BARCODE, generateBarcode());
         db.insert(DatabaseContract.BarcodeEntry.TABLE_NAME, null, values);
      }
   }

   private String generateBarcode() {
      // Generate random barcode
      return UUID.randomUUID().toString();
   }

   private void printBarcodes() {
      if (!mBluetoothAdapter.isEnabled()) {
         Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
         startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
         return;
      }

      Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
      if (pairedDevices.size() > 0) {
         for (BluetoothDevice device : pairedDevices) {
            if (device.getName().equals("MyHandheldPrinter")) {
               mDevice = device;
               break;
            }
                    try {
               mSocket = mDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
               mSocket.connect();
               mOutputStream = mSocket.getOutputStream();

               SQLiteDatabase db = mDbHelper.getReadableDatabase();
               String[] projection = { DatabaseContract.BarcodeEntry.COLUMN_NAME_BARCODE };
               Cursor cursor = db.query(
                  DatabaseContract.BarcodeEntry.TABLE_NAME,
                  projection,
                  null,
                  null,
                  null,
                  null,
                  null
               );

               while (cursor.moveToNext()) {
                  String barcode = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.BarcodeEntry.COLUMN_NAME_BARCODE));
                  mOutputStream.write(barcode.getBytes());
               }

               cursor.close();
               mOutputStream.close();
               mSocket.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (requestCode == REQUEST_ENABLE_BT) {
         if (resultCode == RESULT_OK) {
            printBarcodes();
         } else {
            Toast.makeText(this, "Bluetooth must be enabled to print barcodes", Toast.LENGTH_SHORT).show();
         }
      }
   }
}


