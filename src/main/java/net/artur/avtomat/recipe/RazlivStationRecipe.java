package net.artur.avtomat.recipe;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.artur.avtomat.Avtomat;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class RazlivStationRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;
    private final int Money;
    public RazlivStationRecipe(ResourceLocation id, ItemStack output,
                               NonNullList<Ingredient> recipeItems, int money) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
        this.Money = money;
    }
    public static final Codec<Integer> INT_CODEC = Codec.INT;
    public static int readMoney(JsonObject json) {

            if (json.has("value")) {
                return INT_CODEC.decode(JsonOps.INSTANCE, json.get("value")).result().orElseThrow().getFirst();
            } else {
                System.err.println("JSON ошибка: Отсутствует ключ \"value\" в объекте \"money\"");
                return 0; //  Или другое значение по умолчанию
            }

    }

    public static JsonElement toJson(int money) {
        return INT_CODEC.encodeStart(JsonOps.INSTANCE, money).result().orElseThrow();
    }
    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if(pLevel.isClientSide()) {
            return false;
        }

        return recipeItems.get(0).test(pContainer.getItem(1));
    }

    public int getMoney(){
        return Money;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<RazlivStationRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "razliv_vodi";
    }


    public static class Serializer implements RecipeSerializer<RazlivStationRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Avtomat.MOD_ID, "razliv_vodi");

        @Override
        public RazlivStationRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);
            int money = readMoney(pSerializedRecipe.get("money").getAsJsonObject());
            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new RazlivStationRecipe(pRecipeId, output, inputs, money);
        }

        @Override
        public @Nullable RazlivStationRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);
            int money = buf.readInt();
            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buf));
            }

            ItemStack output = buf.readItem();
            return new RazlivStationRecipe(id, output, inputs, money);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, RazlivStationRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            buf.writeInt(recipe.Money);
            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buf);
            }
            buf.writeItemStack(recipe.getResultItem(), false);
        }
    }
}