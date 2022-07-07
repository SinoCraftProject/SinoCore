package games.moegirl.sinocraft.sinocore.api.utility.texture;

public record TextureEntry(String name, int x, int y, int w, int h, float u, float v, int tw, int th) {

    public Normalized normalized(TextureMap texture) {
        return new Normalized(texture, u, v, tw, th);
    }
}
