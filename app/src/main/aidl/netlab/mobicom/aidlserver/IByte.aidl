// IByte.aidl
package netlab.mobicom.aidlserver;

// Declare any non-default types here with import statements

interface IByte {
    byte[] getByteFromAIDL(String urlString, String sProperty);
}
