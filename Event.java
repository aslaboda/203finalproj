final class Event
{
   public Action action;
   public long time;
   public Entity entity;

   public Event(Action action, long time, Entity entity)
   {
      this.action = action;
      this.time = time;
      this.entity = entity;
   }

   // Student Accessors:
   public Action getAction() {
      return this.action;
   }

   public long getTime() {
      return this.time;
   }

   public Entity getEntity() {
      return this.entity;
   }
}
