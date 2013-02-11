package name.wadewalker.onetriangle;

import java.io.IOException;
import java.net.URL;

import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLProfile;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.PlatformUI;

import com.jogamp.common.util.JarUtil;

public class OneTriangleView extends ViewPart {

    /** Holds the OpenGL canvas. */
    private Composite composite;

    /** Widget that displays OpenGL content. */
    private GLCanvas glcanvas;

    /** Used to get OpenGL object that we need to access OpenGL functions. */
    private GLContext glcontext;

    public OneTriangleView() {
	}

    @Override
    public void createPartControl( Composite compositeParent ) {
        JarUtil.setResolver( new JarUtil.Resolver() {
            public URL resolve( URL url ) {
                try {
                    System.out.printf( "unresolved: %s\n", url );
                    URL urlR = FileLocator.resolve( url );
                    System.out.printf( "resolved: %s\n", urlR );
                    return( urlR ); 
                } catch( IOException ioexception ) {
                    return( url );
                }
            }
        } );

        GLProfile glprofile = GLProfile.get(GLProfile.GL2);

        composite = new Composite( compositeParent, SWT.NONE );
        composite.setLayout( new FillLayout() );

        GLData gldata = new GLData();
        gldata.doubleBuffer = true;
        glcanvas = new GLCanvas( composite, SWT.NO_BACKGROUND, gldata );
        glcanvas.setCurrent();
        glcontext = GLDrawableFactory.getFactory( glprofile ).createExternalGLContext();

        // fix the viewport when the user resizes the window
        glcanvas.addListener( SWT.Resize, new Listener() {
            public void handleEvent(Event event) {
                Rectangle rectangle = glcanvas.getClientArea();
                glcanvas.setCurrent();
                glcontext.makeCurrent();
                OneTriangle.setup( glcontext.getGL().getGL2(), rectangle.width, rectangle.height );
                glcontext.release();        
            }
        });

        (new Thread() {
            public void run() {
                while( (glcanvas != null) && !glcanvas.isDisposed() ) {
                    render();
                    try {
                        // don't make loop too tight, or not enough time
                        // to process window messages properly
                        sleep( 1 );
                    } catch( InterruptedException interruptedexception ) {
                        // we just quit on interrupt, so nothing required here
                    }
                }
            }
        }).start();
    }

    protected void render() {
        PlatformUI.getWorkbench().getDisplay().syncExec( new Runnable() {
            public void run() {
                if( (glcanvas != null) && !glcanvas.isDisposed()) {
                    Rectangle rectangle = glcanvas.getClientArea();
                    glcanvas.setCurrent();
                    glcontext.makeCurrent();
                    OneTriangle.render(glcontext.getGL().getGL2(), rectangle.width, rectangle.height);
                    glcanvas.swapBuffers();
                    glcontext.release();        
                }
            }
        });
    }

    @Override
    public void setFocus() {
    }

    @Override
    public void dispose() {
    	if( glcanvas != null )
    		glcanvas.dispose();
        super.dispose();
    }
}
