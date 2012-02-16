/*
 * Copyright 2011-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.vertx.java.core.eventbus;

import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.logging.LoggerFactory;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
class CharacterMessage extends Message<Character> {

  private static final Logger log = LoggerFactory.getLogger(CharacterMessage.class);

  CharacterMessage(String address, Character payload) {
    super(address, payload);
  }

  public CharacterMessage(Buffer readBuff) {
    super(readBuff);
  }

  protected void readBody(int pos, Buffer readBuff) {
    boolean isNull = readBuff.getByte(pos) == (byte)0;
    if (!isNull) {
      body = (char)readBuff.getShort(++pos);
    }
  }

  protected void writeBody(Buffer buff) {
    if (body == null) {
      buff.appendByte((byte)0);
    } else {
      buff.appendByte((byte)1);
      buff.appendShort((short)body.charValue());
    }
  }

  protected int getBodyLength() {
    return 1 + (body == null ? 0 : 2);
  }

  protected Message copy() {
    // No need to copy since everything is immutable
    return this;
  }

  protected byte type() {
    return TYPE_CHARACTER;
  }

  protected void handleReply(Character reply, Handler<Message<Character>> replyHandler) {
    bus.send(replyAddress, reply, replyHandler);
  }

}
