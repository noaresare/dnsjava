package org.xbill.DNS;

import org.xbill.DNS.utils.base16;

import java.io.IOException;


/**
 * An abstract base class that is used to reduce duplicate code between the
 * different DS record types.
 *
 * @author Brian Wellington
 * @author Dennis Reichenberg
 * @see    DSRecord
 * @see    CDSRecord
 */
public abstract class DSRecordBase extends Record {

static private final long serialVersionUID = -6303497472951402748L;

private int footprint;
private int alg;
private int digestid;
private byte[] digest;

DSRecordBase() { }

public DSRecordBase(Name name, int type, int dclass,
					long ttl, int footprint, int alg,
					int digestid, byte[] digest) {
	super(name, type, dclass, ttl);
	this.footprint = checkU16("footprint", footprint);
	this.alg = checkU8("alg", alg);
	this.digestid = checkU8("digestid", digestid);
	this.digest = digest;
}

/**
 * Returns the key's algorithm.
 */
public int
getAlgorithm() {
	return alg;
}

/**
 * Returns the binary hash of the key.
 */
public byte[]
getDigest() {
	return digest;
}

/**
 * Returns the key's Digest ID.
 */
public int
getDigestID() {
	return digestid;
}

/**
 * Returns the key's footprint.
 */
public int
getFootprint() {
	return footprint;
}

void
rdataFromString(Tokenizer st, Name origin)
	throws IOException {
	footprint = st.getUInt16();
	alg = st.getUInt8();
	digestid = st.getUInt8();
	digest = st.getHex();
}

void
rrFromWire(DNSInput in) throws IOException {
	footprint = in.readU16();
	alg = in.readU8();
	digestid = in.readU8();
	digest = in.readByteArray();
}

/**
 * Converts rdata to a String
 */
String
rrToString() {
	StringBuffer sb = new StringBuffer();
	sb.append(footprint);
	sb.append(" ");
	sb.append(alg);
	sb.append(" ");
	sb.append(digestid);

	if (digest != null) {
		sb.append(" ");
		sb.append(base16.toString(digest));
	}

	return sb.toString();
}

void
rrToWire(DNSOutput out, Compression c,
			  boolean canonical) {
	out.writeU16(footprint);
	out.writeU8(alg);
	out.writeU8(digestid);

	if (digest != null) {
		out.writeByteArray(digest);
	}
}
}