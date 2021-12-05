package me.gleep.oreganized.util.messages;

import me.gleep.oreganized.capabilities.engravedblockscap.EngravedBlocks;
import me.gleep.oreganized.util.GeneralUtility;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Supplier;

public class UpdateClientEngravedBlocks{

    public final HashSet <BlockPos> engravedBlocks;
    public final HashMap <BlockPos, HashMap <EngravedBlocks.Face, String>> engravedFaces;
    public final HashMap <BlockPos, Integer> engravedColors;

    public UpdateClientEngravedBlocks( HashSet <BlockPos> engravedBlocks , HashMap <BlockPos, HashMap <EngravedBlocks.Face, String>> engravedFaces , HashMap <BlockPos, Integer> engravedColors ) {
        this.engravedBlocks = engravedBlocks;
        this.engravedFaces = engravedFaces;
        this.engravedColors = engravedColors;
    }

    public void encode( FriendlyByteBuf buffer ) {
        CompoundTag tag = new CompoundTag();
        int i = 0;
        for(BlockPos pos: engravedBlocks){
            CompoundTag blockpos = new CompoundTag();
            CompoundTag coords = new CompoundTag();
            CompoundTag faces = new CompoundTag();
            coords.putFloat( "X", pos.getX() );
            coords.putFloat( "Y", pos.getY() );
            coords.putFloat( "Z", pos.getZ() );
            blockpos.put( "POS", coords);
            faces.putString( "UP_N", engravedFaces.get(pos).get( EngravedBlocks.Face.UP_N ) == null ? "" : engravedFaces.get(pos).get( EngravedBlocks.Face.UP_N ));
            faces.putString( "DOWN_N", engravedFaces.get(pos).get( EngravedBlocks.Face.DOWN_N ) == null ? "" : engravedFaces.get(pos).get( EngravedBlocks.Face.DOWN_N ) );
            faces.putString( "UP_S", engravedFaces.get(pos).get( EngravedBlocks.Face.UP_S ) == null ? "" : engravedFaces.get(pos).get( EngravedBlocks.Face.UP_S ));
            faces.putString( "DOWN_S", engravedFaces.get(pos).get( EngravedBlocks.Face.DOWN_S ) == null ? "" : engravedFaces.get(pos).get( EngravedBlocks.Face.DOWN_S ) );
            faces.putString( "UP_E", engravedFaces.get(pos).get( EngravedBlocks.Face.UP_E ) == null ? "" : engravedFaces.get(pos).get( EngravedBlocks.Face.UP_E ));
            faces.putString( "DOWN_E", engravedFaces.get(pos).get( EngravedBlocks.Face.DOWN_E ) == null ? "" : engravedFaces.get(pos).get( EngravedBlocks.Face.DOWN_E ) );
            faces.putString( "UP_W", engravedFaces.get(pos).get( EngravedBlocks.Face.UP_W ) == null ? "" : engravedFaces.get(pos).get( EngravedBlocks.Face.UP_W ));
            faces.putString( "DOWN_W", engravedFaces.get(pos).get( EngravedBlocks.Face.DOWN_W ) == null ? "" : engravedFaces.get(pos).get( EngravedBlocks.Face.DOWN_W ) );
            faces.putString( "LEFT", engravedFaces.get(pos).get( EngravedBlocks.Face.LEFT ) == null ? "" : engravedFaces.get(pos).get( EngravedBlocks.Face.LEFT ));
            faces.putString("RIGHT", engravedFaces.get(pos).get( EngravedBlocks.Face.RIGHT ) == null ? "" : engravedFaces.get(pos).get( EngravedBlocks.Face.RIGHT ));
            faces.putString( "FRONT", engravedFaces.get(pos).get( EngravedBlocks.Face.FRONT ) == null ? "" : engravedFaces.get(pos).get( EngravedBlocks.Face.FRONT ));
            faces.putString( "BACK", engravedFaces.get(pos).get( EngravedBlocks.Face.BACK ) == null ? "" : engravedFaces.get(pos).get( EngravedBlocks.Face.BACK ));blockpos.put( "FACES", faces );
            blockpos.putInt( "COLOR", engravedColors.get( pos ) == null ? 0 : engravedColors.get( pos ));
            tag.put( String.valueOf( i ) , blockpos );
            i++;
        }
        tag.putInt("numberOfBlocks", i);
        buffer.writeNbt( tag );
    }

    public static UpdateClientEngravedBlocks decode( FriendlyByteBuf buffer ) {
        HashSet <BlockPos> engravedBlocks = new HashSet <>();
        HashMap <BlockPos, HashMap <EngravedBlocks.Face, String>> engravedFaces = new HashMap <>( 12 );
        HashMap <BlockPos, Integer> engravedColors = new HashMap <>();
        CompoundTag tag = buffer.readAnySizeNbt();
        for( int i = 0; i < tag.getInt( "numberOfBlocks" ); i++){
            CompoundTag block = (CompoundTag) tag.get( String.valueOf( i ) );
            CompoundTag coords = block.getCompound( "POS" );
            CompoundTag faces = block.getCompound( "FACES" );
            BlockPos blockPos = new BlockPos( coords.getFloat( "X" ), coords.getFloat( "Y" ), coords.getFloat( "Z" ) );
            engravedBlocks.add( blockPos );
            HashMap <EngravedBlocks.Face, String> facesmap = new HashMap <EngravedBlocks.Face, String>(12);
            facesmap.put( EngravedBlocks.Face.UP_N, faces.getString( "UP_N" ) );
            facesmap.put( EngravedBlocks.Face.DOWN_N, faces.getString( "DOWN_N" ) );
            facesmap.put( EngravedBlocks.Face.UP_S, faces.getString( "UP_S" ) );
            facesmap.put( EngravedBlocks.Face.DOWN_S, faces.getString( "DOWN_S" ) );
            facesmap.put( EngravedBlocks.Face.UP_E, faces.getString( "UP_E" ) );
            facesmap.put( EngravedBlocks.Face.DOWN_E, faces.getString( "DOWN_E" ) );
            facesmap.put( EngravedBlocks.Face.UP_W, faces.getString( "UP_W" ) );
            facesmap.put( EngravedBlocks.Face.DOWN_W, faces.getString( "DOWN_W" ) );
            facesmap.put( EngravedBlocks.Face.LEFT, faces.getString( "LEFT" ) );
            facesmap.put( EngravedBlocks.Face.RIGHT, faces.getString( "RIGHT" ) );
            facesmap.put( EngravedBlocks.Face.FRONT, faces.getString( "FRONT" ) );
            facesmap.put( EngravedBlocks.Face.BACK, faces.getString( "BACK" ) );
            engravedFaces.put( blockPos, facesmap );
            engravedColors.put( blockPos, block.getInt("COLOR") );
        }
        return new UpdateClientEngravedBlocks(engravedBlocks, engravedFaces , engravedColors );
    }

    public static void handle( UpdateClientEngravedBlocks message, Supplier <NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> GeneralUtility.handleClientEngravedBlocksSync(message));
        context.setPacketHandled(true);
    }
}


