// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: message.proto

package ru.spbau.mit;

public final class ArrayProtos {
  private ArrayProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface ArrayMessageOrBuilder extends
      // @@protoc_insertion_point(interface_extends:mit.ArrayMessage)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>repeated int32 values = 1 [packed = true];</code>
     */
    java.util.List<java.lang.Integer> getValuesList();
    /**
     * <code>repeated int32 values = 1 [packed = true];</code>
     */
    int getValuesCount();
    /**
     * <code>repeated int32 values = 1 [packed = true];</code>
     */
    int getValues(int index);
  }
  /**
   * Protobuf type {@code mit.ArrayMessage}
   */
  public  static final class ArrayMessage extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:mit.ArrayMessage)
      ArrayMessageOrBuilder {
    // Use ArrayMessage.newBuilder() to construct.
    private ArrayMessage(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private ArrayMessage() {
      values_ = java.util.Collections.emptyList();
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private ArrayMessage(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 8: {
              if (!((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
                values_ = new java.util.ArrayList<java.lang.Integer>();
                mutable_bitField0_ |= 0x00000001;
              }
              values_.add(input.readInt32());
              break;
            }
            case 10: {
              int length = input.readRawVarint32();
              int limit = input.pushLimit(length);
              if (!((mutable_bitField0_ & 0x00000001) == 0x00000001) && input.getBytesUntilLimit() > 0) {
                values_ = new java.util.ArrayList<java.lang.Integer>();
                mutable_bitField0_ |= 0x00000001;
              }
              while (input.getBytesUntilLimit() > 0) {
                values_.add(input.readInt32());
              }
              input.popLimit(limit);
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        if (((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
          values_ = java.util.Collections.unmodifiableList(values_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return ru.spbau.mit.ArrayProtos.internal_static_mit_ArrayMessage_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return ru.spbau.mit.ArrayProtos.internal_static_mit_ArrayMessage_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              ru.spbau.mit.ArrayProtos.ArrayMessage.class, ru.spbau.mit.ArrayProtos.ArrayMessage.Builder.class);
    }

    /**
     * Protobuf enum {@code mit.ArrayMessage.Type}
     */
    public enum Type
        implements com.google.protobuf.ProtocolMessageEnum {
      /**
       * <code>DISCONNECT = -1;</code>
       */
      DISCONNECT(-1),
      ;

      /**
       * <code>DISCONNECT = -1;</code>
       */
      public static final int DISCONNECT_VALUE = -1;


      public final int getNumber() {
        return value;
      }

      /**
       * @deprecated Use {@link #forNumber(int)} instead.
       */
      @java.lang.Deprecated
      public static Type valueOf(int value) {
        return forNumber(value);
      }

      public static Type forNumber(int value) {
        switch (value) {
          case -1: return DISCONNECT;
          default: return null;
        }
      }

      public static com.google.protobuf.Internal.EnumLiteMap<Type>
          internalGetValueMap() {
        return internalValueMap;
      }
      private static final com.google.protobuf.Internal.EnumLiteMap<
          Type> internalValueMap =
            new com.google.protobuf.Internal.EnumLiteMap<Type>() {
              public Type findValueByNumber(int number) {
                return Type.forNumber(number);
              }
            };

      public final com.google.protobuf.Descriptors.EnumValueDescriptor
          getValueDescriptor() {
        return getDescriptor().getValues().get(ordinal());
      }
      public final com.google.protobuf.Descriptors.EnumDescriptor
          getDescriptorForType() {
        return getDescriptor();
      }
      public static final com.google.protobuf.Descriptors.EnumDescriptor
          getDescriptor() {
        return ru.spbau.mit.ArrayProtos.ArrayMessage.getDescriptor().getEnumTypes().get(0);
      }

      private static final Type[] VALUES = values();

      public static Type valueOf(
          com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
        if (desc.getType() != getDescriptor()) {
          throw new java.lang.IllegalArgumentException(
            "EnumValueDescriptor is not for this type.");
        }
        return VALUES[desc.getIndex()];
      }

      private final int value;

      private Type(int value) {
        this.value = value;
      }

      // @@protoc_insertion_point(enum_scope:mit.ArrayMessage.Type)
    }

    public static final int VALUES_FIELD_NUMBER = 1;
    private java.util.List<java.lang.Integer> values_;
    /**
     * <code>repeated int32 values = 1 [packed = true];</code>
     */
    public java.util.List<java.lang.Integer>
        getValuesList() {
      return values_;
    }
    /**
     * <code>repeated int32 values = 1 [packed = true];</code>
     */
    public int getValuesCount() {
      return values_.size();
    }
    /**
     * <code>repeated int32 values = 1 [packed = true];</code>
     */
    public int getValues(int index) {
      return values_.get(index);
    }
    private int valuesMemoizedSerializedSize = -1;

    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (getValuesList().size() > 0) {
        output.writeUInt32NoTag(10);
        output.writeUInt32NoTag(valuesMemoizedSerializedSize);
      }
      for (int i = 0; i < values_.size(); i++) {
        output.writeInt32NoTag(values_.get(i));
      }
      unknownFields.writeTo(output);
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      {
        int dataSize = 0;
        for (int i = 0; i < values_.size(); i++) {
          dataSize += com.google.protobuf.CodedOutputStream
            .computeInt32SizeNoTag(values_.get(i));
        }
        size += dataSize;
        if (!getValuesList().isEmpty()) {
          size += 1;
          size += com.google.protobuf.CodedOutputStream
              .computeInt32SizeNoTag(dataSize);
        }
        valuesMemoizedSerializedSize = dataSize;
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof ru.spbau.mit.ArrayProtos.ArrayMessage)) {
        return super.equals(obj);
      }
      ru.spbau.mit.ArrayProtos.ArrayMessage other = (ru.spbau.mit.ArrayProtos.ArrayMessage) obj;

      boolean result = true;
      result = result && getValuesList()
          .equals(other.getValuesList());
      result = result && unknownFields.equals(other.unknownFields);
      return result;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptorForType().hashCode();
      if (getValuesCount() > 0) {
        hash = (37 * hash) + VALUES_FIELD_NUMBER;
        hash = (53 * hash) + getValuesList().hashCode();
      }
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static ru.spbau.mit.ArrayProtos.ArrayMessage parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ru.spbau.mit.ArrayProtos.ArrayMessage parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ru.spbau.mit.ArrayProtos.ArrayMessage parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ru.spbau.mit.ArrayProtos.ArrayMessage parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ru.spbau.mit.ArrayProtos.ArrayMessage parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static ru.spbau.mit.ArrayProtos.ArrayMessage parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static ru.spbau.mit.ArrayProtos.ArrayMessage parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static ru.spbau.mit.ArrayProtos.ArrayMessage parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static ru.spbau.mit.ArrayProtos.ArrayMessage parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static ru.spbau.mit.ArrayProtos.ArrayMessage parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(ru.spbau.mit.ArrayProtos.ArrayMessage prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code mit.ArrayMessage}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:mit.ArrayMessage)
        ru.spbau.mit.ArrayProtos.ArrayMessageOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return ru.spbau.mit.ArrayProtos.internal_static_mit_ArrayMessage_descriptor;
      }

      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return ru.spbau.mit.ArrayProtos.internal_static_mit_ArrayMessage_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                ru.spbau.mit.ArrayProtos.ArrayMessage.class, ru.spbau.mit.ArrayProtos.ArrayMessage.Builder.class);
      }

      // Construct using ru.spbau.mit.ArrayProtos.ArrayMessage.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      public Builder clear() {
        super.clear();
        values_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return ru.spbau.mit.ArrayProtos.internal_static_mit_ArrayMessage_descriptor;
      }

      public ru.spbau.mit.ArrayProtos.ArrayMessage getDefaultInstanceForType() {
        return ru.spbau.mit.ArrayProtos.ArrayMessage.getDefaultInstance();
      }

      public ru.spbau.mit.ArrayProtos.ArrayMessage build() {
        ru.spbau.mit.ArrayProtos.ArrayMessage result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public ru.spbau.mit.ArrayProtos.ArrayMessage buildPartial() {
        ru.spbau.mit.ArrayProtos.ArrayMessage result = new ru.spbau.mit.ArrayProtos.ArrayMessage(this);
        int from_bitField0_ = bitField0_;
        if (((bitField0_ & 0x00000001) == 0x00000001)) {
          values_ = java.util.Collections.unmodifiableList(values_);
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.values_ = values_;
        onBuilt();
        return result;
      }

      public Builder clone() {
        return (Builder) super.clone();
      }
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.setField(field, value);
      }
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof ru.spbau.mit.ArrayProtos.ArrayMessage) {
          return mergeFrom((ru.spbau.mit.ArrayProtos.ArrayMessage)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(ru.spbau.mit.ArrayProtos.ArrayMessage other) {
        if (other == ru.spbau.mit.ArrayProtos.ArrayMessage.getDefaultInstance()) return this;
        if (!other.values_.isEmpty()) {
          if (values_.isEmpty()) {
            values_ = other.values_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureValuesIsMutable();
            values_.addAll(other.values_);
          }
          onChanged();
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        ru.spbau.mit.ArrayProtos.ArrayMessage parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (ru.spbau.mit.ArrayProtos.ArrayMessage) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.util.List<java.lang.Integer> values_ = java.util.Collections.emptyList();
      private void ensureValuesIsMutable() {
        if (!((bitField0_ & 0x00000001) == 0x00000001)) {
          values_ = new java.util.ArrayList<java.lang.Integer>(values_);
          bitField0_ |= 0x00000001;
         }
      }
      /**
       * <code>repeated int32 values = 1 [packed = true];</code>
       */
      public java.util.List<java.lang.Integer>
          getValuesList() {
        return java.util.Collections.unmodifiableList(values_);
      }
      /**
       * <code>repeated int32 values = 1 [packed = true];</code>
       */
      public int getValuesCount() {
        return values_.size();
      }
      /**
       * <code>repeated int32 values = 1 [packed = true];</code>
       */
      public int getValues(int index) {
        return values_.get(index);
      }
      /**
       * <code>repeated int32 values = 1 [packed = true];</code>
       */
      public Builder setValues(
          int index, int value) {
        ensureValuesIsMutable();
        values_.set(index, value);
        onChanged();
        return this;
      }
      /**
       * <code>repeated int32 values = 1 [packed = true];</code>
       */
      public Builder addValues(int value) {
        ensureValuesIsMutable();
        values_.add(value);
        onChanged();
        return this;
      }
      /**
       * <code>repeated int32 values = 1 [packed = true];</code>
       */
      public Builder addAllValues(
          java.lang.Iterable<? extends java.lang.Integer> values) {
        ensureValuesIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, values_);
        onChanged();
        return this;
      }
      /**
       * <code>repeated int32 values = 1 [packed = true];</code>
       */
      public Builder clearValues() {
        values_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
        return this;
      }
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:mit.ArrayMessage)
    }

    // @@protoc_insertion_point(class_scope:mit.ArrayMessage)
    private static final ru.spbau.mit.ArrayProtos.ArrayMessage DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new ru.spbau.mit.ArrayProtos.ArrayMessage();
    }

    public static ru.spbau.mit.ArrayProtos.ArrayMessage getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    @java.lang.Deprecated public static final com.google.protobuf.Parser<ArrayMessage>
        PARSER = new com.google.protobuf.AbstractParser<ArrayMessage>() {
      public ArrayMessage parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
          return new ArrayMessage(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<ArrayMessage> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<ArrayMessage> getParserForType() {
      return PARSER;
    }

    public ru.spbau.mit.ArrayProtos.ArrayMessage getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_mit_ArrayMessage_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_mit_ArrayMessage_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\rmessage.proto\022\003mit\"C\n\014ArrayMessage\022\022\n\006" +
      "values\030\001 \003(\005B\002\020\001\"\037\n\004Type\022\027\n\nDISCONNECT\020\377" +
      "\377\377\377\377\377\377\377\377\001B\033\n\014ru.spbau.mitB\013ArrayProtos"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_mit_ArrayMessage_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_mit_ArrayMessage_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_mit_ArrayMessage_descriptor,
        new java.lang.String[] { "Values", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
