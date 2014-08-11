
package dan200.billund.client.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelParachute extends ModelBase {

    public ModelRenderer parachute;
    public ModelRenderer leftWing;
    public ModelRenderer rightWing;
    public ModelRenderer wire1;
    public ModelRenderer wire2;
    public ModelRenderer wire3;
    public ModelRenderer wire4;

    private static void setPosition(ModelRenderer m, float x, float y, float z) {
        m.offsetX = x * 0.0625f;
        m.offsetY = y * 0.0625f;
        m.offsetZ = z * 0.0625f;
    }

    private static void setRotation(ModelRenderer m, float x, float y, float z) {
        m.rotateAngleX = x;
        m.rotateAngleY = y;
        m.rotateAngleZ = z;
    }

    public ModelParachute() {
        parachute = new ModelRenderer(this, 0, 0);
        parachute.addBox(-6.0F, -0.5F, -6.0F, 12, 1, 12, 0);
        setPosition(parachute, 0.0F, -10.0F, -0.0f);
        setRotation(parachute, 0.0f, 0.0f, 0.0f);

        leftWing = new ModelRenderer(this, 0, 0);
        leftWing.addBox(-3.0F, -0.5F, -6.0F, 6, 1, 12, 0);
        setPosition(leftWing, -8.5F, -8.5F, -0.0f);
        setRotation(leftWing, 0.0f, 0.0f, -10.0f);

        rightWing = new ModelRenderer(this, 0, 0);
        rightWing.addBox(-3.0F, -0.5F, -6.0F, 6, 1, 12, 0);
        setPosition(rightWing, 8.5F, -8.5F, -0.0f);
        setRotation(rightWing, 0.0f, 0.0f, 10.0f);

        wire1 = new ModelRenderer(this, 0, 0);
        wire1.addBox(-0.5F, -5.0F, -0.5F, 1, 10, 1, 0);
        setPosition(wire1, -6.0F, -4.0F, -4.0f);

        wire2 = new ModelRenderer(this, 0, 0);
        wire2.addBox(-0.5F, -5.0F, -0.5F, 1, 10, 1, 0);
        setPosition(wire2, -6.0F, -4.0F, 4.0f);

        wire3 = new ModelRenderer(this, 0, 0);
        wire3.addBox(-0.5F, -5.0F, -0.5F, 1, 10, 1, 0);
        setPosition(wire3, 6.0F, -4.0F, -4.0f);

        wire4 = new ModelRenderer(this, 0, 0);
        wire4.addBox(-0.5F, -5.0F, -0.5F, 1, 10, 1, 0);
        setPosition(wire4, 6.0F, -4.0F, 4.0f);
    }

    public void render(float f5) {
        parachute.render(f5);
        leftWing.render(f5);
        rightWing.render(f5);
        wire1.render(f5);
        wire2.render(f5);
        wire3.render(f5);
        wire4.render(f5);
    }
}
