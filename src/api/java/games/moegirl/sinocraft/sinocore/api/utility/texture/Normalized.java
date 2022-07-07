package games.moegirl.sinocraft.sinocore.api.utility.texture;

public record Normalized(float u, float v, float w, float h) {

    Normalized(TextureMap texture, float u, float v, float w, float h) {
        this(u / texture.width, v / texture.height, w / texture.width, h / texture.height);
    }
}
