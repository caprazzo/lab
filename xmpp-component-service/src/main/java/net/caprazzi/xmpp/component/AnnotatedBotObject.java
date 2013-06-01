package net.caprazzi.xmpp.component;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import net.caprazzi.xmpp.BotContext;
import net.caprazzi.xmpp.BotEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.Packet;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;

/**
* Created with IntelliJ IDEA.
* User: mcaprari
* Date: 30/05/13
* Time: 17:45
* To change this template use File | Settings | File Templates.
*/
public class AnnotatedBotObject {

    private final Logger Log = LoggerFactory.getLogger(AnnotatedBotObject.class);
    private final Class<?> clazz;
    private final Object obj;
    private final Collection<Field> injecatbleFields;
    private final Collection<ReceiverMethod> receiverMethods;

    AnnotatedBotObject(Object obj) {
        Preconditions.checkNotNull(obj, "Bot object must not be null.");
        this.obj = obj;
        this.clazz = obj.getClass();
        injecatbleFields = scanInjectableFields();
        receiverMethods = scanReceiverMethods();
    }

    public static Optional<AnnotatedBotObject> from(Object obj) {
        Preconditions.checkNotNull(obj, "Bot object must not be null.");
        AnnotatedBotObject annotated = new AnnotatedBotObject(obj);
        if (annotated.injecatbleFields.size() > 0 || annotated.receiverMethods.size() > 0) {
            return Optional.of(annotated);
        }
        return Optional.absent();
    }

    private Collection<Field> scanInjectableFields() {
        ArrayList<Field> injectables = new ArrayList<Field>();
        for(Field field : ReflectionUtils.fields(clazz)) {
            if (isInjectable(field)) {
                Log.debug("Found injectable field: {}", field);
                injectables.add(field);
            }
        }
        Log.debug("Found {} injectable fields", injectables);
        return injectables;
    }

    private Collection<ReceiverMethod> scanReceiverMethods() {
        ArrayList<ReceiverMethod> receivers = new ArrayList<ReceiverMethod>();
        for(Method method : ReflectionUtils.methods(clazz)) {
            Optional<ReceiverMethod> receiver = ReceiverMethod.from(method);
            if (receiver.isPresent()) {
                Log.debug("Found receiver method: {}", method);
                receivers.add(receiver.get());
            }
        }
        Log.debug("Found {} receiver methods", receivers.size());
        return receivers;
    }

    private boolean isInjectable(Field field) {
        BotContext annotation = field.getAnnotation(BotContext.class);
        if (annotation == null) {
            return false;
        }
        if (BotEnvironment.class.isAssignableFrom(field.getType())) {
            return true;
        }
        else {
            Log.debug("Field marked @BotContext is not valid because its type is not supported : {}", field);
            return false;
        }
    }

    public Optional<Packet> receive(Packet packet) {
        for (ReceiverMethod method : receiverMethods) {
            if (method.canReceive(packet)) {
                return method.receive(obj, packet);
            }
        }
        return Optional.absent();
    }

    public void inject(Object value) {
        for(Field field : injecatbleFields) {
            if (!Modifier.isPublic(field.getModifiers())) {
                field.setAccessible(true);
            }
            Log.debug("Injecting {} to field {}", value, field);

            if (!field.getType().isAssignableFrom(value.getClass())) {
                Log.debug("Injected value is not compatible with field {}", field);
                return;
            }

            try {
                field.set(obj, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
